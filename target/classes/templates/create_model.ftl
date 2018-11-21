<#assign title='CREATE MODEL'>
<#assign model='active'>
<#include 'header.ftl'>
    <main class="main-body">
      <div class="col-xs-12 col-lg-12">
        <div class="col-md-5 col-xs-12">
          <div class="row padding-20-10 box-left">
            <div class="form-group col-lg-6 col-xs-6">
              <div class="input-group">
                <label class="input-group-addon">Name: </label>
                <input name="name" type="text" class="form-control">
              </div>
            </div>
            <div class="form-group col-lg-6 col-xs-6">
              <div class="input-group">
                <label class="input-group-addon">Author: </label>
                <input name="author" type="text" class="form-control">
              </div>
            </div>
            <div class="form-group col-lg-6 col-xs-6">
              <div class="input-group">
                <label class="input-group-addon">Privacy: </label>
                <select name="privacy" class="form-control">
                  <option value="true" selected>privacy</option>
                  <option value="false">no privacy</option>
                </select>
              </div>
            </div>
            <div class="form-group col-lg-6 col-xs-6">
              <div class="input-group">
                <label class="input-group-addon">Price: </label>
                <input name="price" type="number" class="form-control">
              </div>
            </div>
            <div class="col-xs-12">
              <div style="padding: 10px;">
                <div style="font-size: 18px">
                  <span>Chose a file to upload&nbsp;</span>
                  <input type="file" id="file3d" style="display: none;" accept="*.fbx,*.obj,*.collada,*.glb">
                  <button id="chooseFile" class="btn btn-sm thin-btn">file</button>
                </div>
                <div>
                  <span class="text-tip">*support .fbx, .glb</span>
                </div>
              </div>
            </div>
            <div class="col-xs-12">
              <div class="padding-20-10">
                <h4>Select Category Tag</h4>
                <div>
                  <span class="label label-success">Animal</span>
                  <span class="label label-success">Sport</span>
                  <span class="label label-success">Electronic</span>
                  <span class="label label-success">Natrual</span>
                </div>
                <h4>Select Device Tag</h4>
                <div>
                  <span class="label label-warning">Mobile</span>
                  <span class="label label-warning">PC</span>
                  <span class="label label-warning">Hololens</span>
                  <span class="label label-warning">Workstation</span>
                </div>
              </div>
            </div>
          </div>
          <div class="padding-10 text-center">
            <button id="createBtn" class="btn btn-info btn-block">create</button>
          </div>
        </div>

        <div class="col-md-7 col-xs-12">
          <div class="padding-20-10 box-right">
            <div class="photo-box">
                <div id="canvas" width="100%" alt="" ></div>
            </div>
          </div>
          <div class="padding-10 text-right">
            <button class="btn btn-success">capture this moment generate thumnall</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</div>

<script src="../static/js/three.js"></script>

<script src="../static/js/libs/inflate.min.js"></script>

<script src="../static/js/loaders/FBXLoader.js"></script>
<script src="../static/js/loaders/ColladaLoader.js"></script>
<script src="../static/js/loaders/OBJLoader.js"></script>
<script src="../static/js/loaders/GLTFLoader.js"></script>

<script src="../static/js/controls/OrbitControls.js"></script>

<script src="../static/js/WebGL.js"></script>
<script src="../static/js/libs/stats.min.js"></script>
</body>
</html>