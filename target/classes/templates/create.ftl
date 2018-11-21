<#-- User: soft Date: 2018/11/14 19:08 -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.js"></script>
  <script src="https://cdn.bootcss.com/layer/2.3/layer.js"></script>
  <script src="https://cdn.bootcss.com/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>

  <link rel="stylesheet" href="https://cdn.bootcss.com/twitter-bootstrap/3.3.6/css/bootstrap.min.css">
  <style>
    .top-20 {
      padding-top: 20px;
    }
    .progress {
      margin-bottom: 0;
    }
  </style>
  <title>xrhub creator</title>
</head>
<body>
<div class="container top-20">
  <div class="panel panel-info">
    <div class="panel-heading">
      <div class="panel-title">Info Sheet</div>
    </div>
    <div class="panel-body">
      <div>
        <form class="form-horizontal">
          <div class="form-group col-lg-6 col-sm-12">
            <label for="username" class="control-label col-lg-2 col-sm-2 col-xs-4">Name</label>
            <div class="col-lg-10 col-sm-10">
              <input name="name" id="username" class="form-control" placeholder="Name" required="">
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="author" class="control-label col-lg-2 col-sm-2 col-xs-4">Author</label>
            <div class="col-lg-10 col-sm-10">
              <input name="authorId" id="author" class="form-control" placeholder="Author" required="">
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="privacy" class="control-label col-lg-2 col-sm-2 col-xs-4">Privacy</label>
            <div class="col-lg-10 col-sm-10">
              <select name="privacy" id="privacy" class="form-control">
                <option value="true" selected>Privacy</option>
                <option value="false">No Privacy</option>
              </select>
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="price" class="control-label col-lg-2 col-sm-2 col-xs-4">Price</label>
            <div class="col-lg-10 col-sm-10">
              <input name="price" id="price" type="number" class="form-control" placeholder="Price" required="">
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="typeName" class="control-label col-lg-2 col-sm-2 col-xs-4">Type</label>
            <div class="col-lg-10 col-sm-10">
              <select name="type" id="typeName" class="form-control">
                <option value="0">ELEMENT</option>
                <option value="1">CREATION</option>
                <option value="2">DOME</option>
                <option value="3">MATERIALS</option>
              </select>
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="thumbnailId" class="control-label col-lg-2 col-sm-2 col-xs-4">Path</label>
            <div class="col-lg-10 col-sm-10">
              <input name="thumbnailId" id="thumbnailId" class="form-control" placeholder="Thumbnail Path" required="">
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="file" class="control-label col-lg-2 col-sm-2 col-xs-4">Meta</label>
            <div class="col-lg-10 col-sm-10">
              <input name="file" id="file" type="file" required="">
            </div>
          </div>
          <div class="form-group col-lg-6 col-sm-12">
            <label for="thumbnail" class="control-label col-lg-2 col-sm-2 col-xs-4">Thumbnail</label>
            <div class="col-lg-10 col-sm-10">
              <input name="thumbnail" id="thumbnail" type="file" required="">
            </div>
          </div>
          <input name="visits" value="0" type="hidden">
          <input name="collects" value="0" type="hidden">
          <input name="downloads" value="0" type="hidden">
          <input name="vertices" value="0" type="hidden">
          <input name="triangles" value="0" type="hidden">
          <input name="resolution" value="0" type="hidden">
          <div>
              <button class="btn btn-info btn-block" type="submit">CREATE</button>
          </div>
        </form>
      </div>
    </div>
  </div>
  <div id="prog" style="display: none;">
    <div class="progress progress-striped active">
      <div class="progress-bar progress-bar-success"></div>
    </div>
  </div>
  <script>
    $('form').submit(function (e) {
      e.preventDefault();
      var self = e.target;
      var fd = new FormData(self);
      var xhr = new XMLHttpRequest();
      var progi = layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        area: ['500px', '20px'], //宽高
        content: $('#prog')
      });
      xhr.open("post", "/store/build", true);
      xhr.onload = function (ev) {
        layer.close(progi);
        var data = JSON.parse(xhr.responseText);
        if (data.success) {
          layer.alert("asset header index: <br>" + data.payload, {title: 'SUCCESS', icon: 6}, function (index) {
            self.reset();
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
    });
  </script>
</div>
</body>
</html>