# Matcher App

A dating app

## Project Overview

The goal is create app that allows users match and chat with each other. In this project, use API
from server Java Spring Boot [in here](https://github.com/SanRyoo/Matcher_Server.git)

## API_URL and WS_URL

You need to insert your URL. Go to file "local.properties" and add:

```properties
API_URL="http://<YOUR ADDRESS>:8088"
WS_URL="ws://<YOUR ADDRESS>:8088/matcher/websocket"

# Example: 
# API_URL="http://a.b.c.d:8080"
# WS_URL="ws://a.b.c.d:8080/matcher/websocket"

```

## Features

- Dependency injection (use Hilt dagger)
- Call API: log in, sign up, update, ... (use Retrofit)
- Find match and send message (use Web Socket client refer Stomp Client of
  bishoybasily [in here](https://github.com/bishoybasily/stomp.git))
- Log in with Google, Facebook
- Observe connectivity (show in Snack bar when change)
- Observe location
- Navigation compose
- Bottom navigation
- Bottom sheet
- Load image (use Coil)
- Share preference (save last id log in)
- Data store(save token(with key store and cipher) and data user)

## Screen short

### Log in

<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/option_login.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/log_in.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/sign_up.png" style="width:32%;"> 
</div>

### Using

<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/home.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/home2.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/profile.png" style="width:32%;"> 
</div>
<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/menu.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/change_password.png" style="width:32%;">
</div>
<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/message.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Image/master/other_screen.png" style="width:32%;">
</div>