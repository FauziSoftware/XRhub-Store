<#-- User: soft Date: 2018/11/18 20:31 -->
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <script src="https://cdn.staticfile.org/jquery/3.3.1/jquery.js"></script>
  <script src="https://cdn.staticfile.org/layer/2.3/layer.js"></script>
  <script src="/static/js/index.js"></script>
  <link rel="stylesheet" href="/static/css/style.css">
  <link rel="stylesheet" href="/static/bootstrap.css">
  <title>${title!'CREATE MODEL'}</title>
  <style>
    .progress {
      margin-bottom: 0;
    }
  </style>
</head>
<body>
<div id="prog" style="display: none;">
  <div class="progress progress-striped active">
    <div class="progress-bar progress-bar-success"></div>
  </div>
</div>
<div class="container">
  <div style="margin: 40px auto;">
    <ul class="nav nav-tabs black-ab">
      <li class="${model!''}">
        <a href="/asset/model">
          <span>MODEL</span>
        </a>
      </li>
      <li class="${scene!''}">
        <a href="/asset/scene">
          <span>SCENE</span>
        </a>
      </li>
      <li class="${skybox!''}">
        <a href="/asset/skybox">
          <span>SKYBOX</span>
        </a>
      </li>
      <li class="${material!''}">
        <a href="/asset/material">
          <span>MATERIAL</span>
        </a>
      </li>
    </ul>