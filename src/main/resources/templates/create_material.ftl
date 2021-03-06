<#assign title='CREATE MATERIAL'>
<#assign material='active'>
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
                  <option value="0">privacy</option>
                  <option value="1">public</option>
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
                  <span class="text-tip">*support .jpeg, .png, .mp4</span>
                </div>
              </div>
            </div>
            <div class="col-xs-12">
              <div class="padding-20-10">
                <h4>标签</h4>
                <div>
                  <span class="label label-success">标签</span>
                  <span class="label label-success">标签</span>
                  <span class="label label-success">标签</span>
                  <span class="label label-success">标签</span>
                </div>
                <h4>设备兼容</h4>
                <div>
                  <span class="label label-warning">兼容</span>
                  <span class="label label-warning">兼容</span>
                  <span class="label label-warning">兼容</span>
                  <span class="label label-warning">兼容</span>
                </div>
              </div>
            </div>
          </div>
          <div class="padding-10 text-center">
            <button class="btn btn-info btn-block">create</button>
          </div>
        </div>
        <div class="col-md-7 col-xs-12">
          <div class="padding-20-10 box-right">
            <div class="photo-box">
              <img src="/static/pp.png" width="100%" alt="">
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
</body>
</html>