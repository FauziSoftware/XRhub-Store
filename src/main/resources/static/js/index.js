/**
 * @AUTHOR soft
 * @DATE 2018/11/19 20:35
 * @DESCRIBE
 */
$(function () {
    var $fileInp = $('#file3d');
    var $chooseBtn = $('#chooseFile');
    $chooseBtn.click(function (e) {
        $fileInp.click();
    });

    $fileInp.change(function (e) {
        var file = e.target.files[0];
        console.log(file);
        $chooseBtn.html(file.name);
    });

    $('#createBtn').click(function () {
        uploadToCreate();
    });

    //Init WebGL Scene
    if (WEBGL.isWebGLAvailable() === false) {
        document.body.appendChild(WEBGL.getWebGLErrorMessage());
    }
    var container, stats, controls;
    var camera, scene, renderer, light,envMap;
    var clock = new THREE.Clock();
    var mixers = [];
    initScene();
    animate();
    //

    //Load 3D Model Into Scene
    var inp = document.querySelector('#file3d');
    inp.onchange = function () {
        console.log('change');
        var file = inp.files[0];
        var urlObj = URL.createObjectURL(file);
        console.log(urlObj);
        var filename = file.name;
        if (isType(filename, 'fbx')) {
            fbxLoader(urlObj);
        } else if (isType(filename, 'glb')) {
            glbLoader(urlObj);
        }
    }

    function initScene() {
        container = document.getElementById('canvas');
        camera = new THREE.PerspectiveCamera(45, window.innerWidth / window.innerHeight, 0.01, 1000);
        camera.position.set(2.5, 2.5, 2.5);
        controls = new THREE.OrbitControls(camera);
        controls.target.set(0, 0, 0);
        controls.update();
        scene = new THREE.Scene();
        // envmap
        var path = '/static/textures/cube/Bridge2/';
        var format = '.jpg';
        envMap = new THREE.CubeTextureLoader().load([
            path + 'posx' + format, path + 'negx' + format,
            path + 'posy' + format, path + 'negy' + format,
            path + 'posz' + format, path + 'negz' + format
        ]);
        //
        scene.background = envMap;
        light = new THREE.HemisphereLight(0xffffff, 0x444444);
        light.position.set(0, 200, 0);
        scene.add(light);
        light = new THREE.DirectionalLight(0xffffff);
        light.position.set(0, 200, 100);
        light.castShadow = true;
        light.shadow.camera.top = 180;
        light.shadow.camera.bottom = -100;
        light.shadow.camera.left = -120;
        light.shadow.camera.right = 120;
        scene.add(light);

        var mesh = new THREE.Mesh( new THREE.PlaneBufferGeometry( 2000, 2000 ), new THREE.MeshPhongMaterial( { color: 0x999999, depthWrite: false } ) );
        mesh.rotation.x = - Math.PI / 2;
        mesh.receiveShadow = true;
        scene.add( mesh );
        var grid = new THREE.GridHelper( 10, 20, 0x000000, 0x000000 );
        grid.material.opacity = 0.2;
        grid.material.transparent = true;
        scene.add( grid );

        renderer = new THREE.WebGLRenderer({antialias: true});
        renderer.setPixelRatio(window.devicePixelRatio);
        renderer.setSize(500, 350);
        renderer.shadowMap.enabled = true;
        container.appendChild(renderer.domElement);
        window.addEventListener('resize', onWindowResize, false);

        stats = new Stats();
        container.appendChild(stats.dom);
    }

    function fbxLoader(url) {
        var loader = new THREE.FBXLoader();
        loader.load(url, function (object) {
            object.mixer = new THREE.AnimationMixer(object);
            mixers.push(object.mixer);
            var action = object.mixer.clipAction(object.animations[0]);
            action.play();
            object.traverse(function (child) {
                if (child.isMesh) {
                    child.castShadow = true;
                    child.receiveShadow = true;
                }
            });
            var size = new THREE.Box3().setFromObject(object).getSize();
            object.scale.set( 1/size.x, 1/size.x, 1/size.x );
            object.position.y += new THREE.Box3().setFromObject(object).getSize().y * 0.5;
            scene.add(object);
        });
    }

    function glbLoader(url) {
        var loader = new THREE.GLTFLoader();
        loader.load( url, function ( gltf ) {
            gltf.scene.traverse( function ( child ) {
                if ( child.isMesh ) {
                    child.material.envMap = envMap;
                }
            } );
            var size = new THREE.Box3().setFromObject(gltf.scene).getSize() // Returns Vector3
            gltf.scene.scale.set( 1/size.x, 1/size.x, 1/size.x );
            gltf.scene.position.y += new THREE.Box3().setFromObject(gltf.scene).getSize().y * 0.5;
            scene.add( gltf.scene );
        }, undefined, function ( e ) {
            console.error( e );
        } );
    }
    
    function CountVertices() {
        scene.children.traverse(function (child) {
            if(child.isMesh){
                console.log(child.name);
            }
        })
    }

    // function colladaLoader(url) {
        // var loadingManager = new THREE.LoadingManager( function () {
        //     scene.add( elf );
        // } );
        // // collada
        // var loader = new THREE.ColladaLoader( loadingManager );
        // loader.load( url, function ( collada ) {
        //     elf = collada.scene;
        // } );
    // }

    // function objLoader(url) {
        // function loadModel() {
        //     object.traverse(function (child) {
        //         if (child.isMesh) child.material.map = texture;
        //     });
        //     object.position.y = -95;
        //     scene.add(object);
        // }
        //
        // var manager = new THREE.LoadingManager(loadModel);
        // manager.onProgress = function (item, loaded, total) {
        //     console.log(item, loaded, total);
        // };
        //
        // var textureLoader = new THREE.TextureLoader(manager);
        // var texture = textureLoader.load('textures/UV_Grid_Sm.jpg');
        //
        // function onProgress(xhr) {
        //     if (xhr.lengthComputable) {
        //         var percentComplete = xhr.loaded / xhr.total * 100;
        //         console.log('model ' + Math.round(percentComplete, 2) + '% downloaded');
        //     }
        // }
        //
        // function onError() {
        // }
        //
        // var loader = new THREE.OBJLoader(manager);
        // loader.load(url, function (obj) {
        //     object = obj;
        // }, onProgress, onError);
    // }

    // function exportGLTF(input) {
    //     var gltfExporter = new THREE.GLTFExporter();
    //     var options = {
    //         trs: false,
    //         onlyVisible: true,
    //         truncateDrawRange: false,
    //         binary: true,
    //         forceIndices: true,
    //         forcePowerOfTwoTextures: true,
    //     };
    //     gltfExporter.parse(input, function (result) {
    //         if (result instanceof ArrayBuffer) {
    //             saveArrayBuffer(result, 'scene.glb');
    //         } else {
    //             var output = JSON.stringify(result, null, 2);
    //             console.log(output);
    //             saveString(output, 'scene.gltf');
    //         }
    //     }, options);
    // }

    function saveString(text, filename) {
        save(new Blob([text], {type: 'text/plain'}), filename);
    }

    function saveArrayBuffer(buffer, filename) {
        save(new Blob([buffer], {type: 'application/octet-stream'}), filename);
    }

    function onWindowResize() {
        camera.aspect = window.innerWidth / window.innerHeight;
        camera.updateProjectionMatrix();
        renderer.setSize(window.innerWidth, window.innerHeight);
    }

    function animate() {
        requestAnimationFrame(animate);
        if (mixers.length > 0) {
            for (var i = 0; i < mixers.length; i++) {
                mixers[i].update(clock.getDelta());
            }
        }
        renderer.render(scene, camera);
        stats.update();
    }

    // function shot() {
    //     var image = new Image();
    //     renderer.render(scene, camera);//此处renderer为three.js里的渲染器，scene为场景 camera为相机
    //
    //     var imgData = renderer.domElement.toDataURL("image/jpeg");//这里可以选择png格式jpeg格式
    //     image.src = imgData;
    //     document.body.appendChild(image);//这样就可以查看截出来的图片了
    // }

    var link = document.createElement('a');
    link.style.display = 'none';
    document.body.appendChild(link); // Firefox workaround, see #6594
    function save(blob, filename) {
        link.href = URL.createObjectURL(blob);
        link.download = filename;
        link.click();
        // URL.revokeObjectURL( url ); breaks Firefox...
    }

    function uploadToCreate() {
        var fd = objToFd(getFormValue(2));
        var file = $('#file3d')[0].files[0];
        get3DFile(file, function (file3d) {
            fd.append('file', file3d); // 若file3d没有文件名，则在第三个参数填上 下同
            getThumbnail(function (thumbnail) {
                fd.append('image', thumbnail);

                var xhr = new XMLHttpRequest();
                var progi = layer.open({ // 显示进度条
                    type: 1,
                    title: false,
                    closeBtn: 0,
                    area: ['500px', '20px'],
                    content: $('#prog')
                });
                xhr.open("post", "/store/build", true);
                xhr.onload = function (ev) {
                    layer.close(progi); // 关闭进度条
                    var data = JSON.parse(xhr.responseText);
                    if (data.success) {
                        layer.alert("asset header index: <br>" + data.payload, {
                            title: 'SUCCESS',
                            icon: 6
                        }, function (index) {
                            layer.close(index);
                        });
                    } else {
                        layer.alert("cause: <br>" + data.msg, {title: 'FAIL', icon: 2}, function (index) {
                            layer.close(index);
                        });
                    }
                };
                xhr.upload.onprogress = function (ev) {
                    var pn = parseInt(ev.loaded / ev.total * 100);
                    var pg = $('#prog');
                    if (ev.loaded === ev.total) {
                        pg.find('.progress-bar').css('width', '100%').html("converting...");
                        pg.find('.progress').addClass('active');
                    } else {
                        pg.find('.progress-bar').css('width', pn + '%').html(pn + '%');
                    }
                };
                xhr.send(fd);
            })
        });
    }

    /**
     * 获取表单值
     * 传入上传文件的类型 模型，天空盒等
     * @param type 类型 0-ELEMENT 1-CREATION 2-DOME 3-MATERIALS
     */
    function getFormValue(type) {
        var name = document.querySelector('input[name=name]').value;
        var author = document.querySelector('input[name=author]').value;
        var price = document.querySelector('input[name=price]').value;
        var privacy = document.querySelector('select[name=privacy]').value;

        return {
            visits: 0, collects: 0, downloads: 0, vertices: 0, triangles: 0, resolution: 0,
            type: type, name: name, authorId: author, price: price, privacy: privacy
        };
    }

    /**
     * 把object类型表单转换为formData类型
     * @param obj
     */
    function objToFd(obj) {
        var fd = new FormData();
        if (obj instanceof Object) {
            for (var k in obj) {
                fd.append(k, obj[k]);
            }
        }
        return fd;
    }

    /**
     * 判断是不是目标类型
     * @param filename
     * @param type
     */
    function isType(filename, type) {
        return filename.indexOf('.' + type, filename.lastIndexOf('.')) !== -1;
    }

    /**
     * 获取3D模型文件 用于上传
     * @param file 表单中的文件
     * @param callback 回调函数，获取后回调 传参为文件二进制
     */
    function get3DFile(file, callback) {
        // 获取文件代码

        var final = null;
        var gltfExporter = new THREE.GLTFExporter();
        var options = {
            trs: false,
            onlyVisible: true,
            truncateDrawRange: false,
            binary: true,
            forceIndices: true,
            forcePowerOfTwoTextures: true,
        };
        gltfExporter.parse(input, function (result) {
            if (result instanceof ArrayBuffer) {
                saveArrayBuffer(result, 'scene.glb');
                final = new Blob(result);
            } else {
                var output = JSON.stringify(result, null, 2);
                console.log(output);
                saveString(output, 'scene.gltf');
            }
        }, options);

        callback ? callback(final) : null;
    }

    /**
     * 获取缩略图 用于上传
     * @param callback 回调函数，获取后回调 传参为文件二进制
     */
    function getThumbnail(callback) {
        // 获取缩略图代码
        var file = null;
        var image = new Image();
        renderer.render(scene, camera);//此处renderer为three.js里的渲染器，scene为场景 camera为相机
        var imgData = renderer.domElement.toDataURL("image/jpeg");//这里可以选择png格式jpeg格式
        file = new Blob(image.arrayBuffer);

        callback ? callback(file) : null;
    }
});


