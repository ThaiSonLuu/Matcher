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
# API_URL="http://a.b.c.d:8088"
# WS_URL="ws://a.b.c.d:8088/matcher/websocket"

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
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/option_login.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/log_in.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/sign_up.png" style="width:32%;"> 
</div>

### Using

<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/home.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/home2.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/profile.png" style="width:32%;"> 
</div>
<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/menu.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/change_password.png" style="width:32%;">
</div>
<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/message.png" style="width:32%;">
    <img src="https://raw.githubusercontent.com/SanRyoo/Matcher/master/screenshots/other_screen.png" style="width:32%;">
</div>
