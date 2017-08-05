# Location
android 定位
#注意
目前只变编写了使用Android系统的定位方式定位和获取地理位置信息的反编码数据
但是Android系统Geocoder存在严重bug,在获取地理位置的信息时不一定能获取到反
地理编码信息，所以一般采用谷歌提供的Geocoder api(支持json、xml格式)
http://maps.googleapis.com/maps/api/geocode/json?latlng=31.293767,121.42165&sensor=false
或者百度地图服务端提供的Geocoder api(支持json、xml格式) ak需要自己申请服务端的ak
http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=31.293767,121.42165&output=json&pois=10&ak=
     
