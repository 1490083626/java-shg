/**
 * 
 */
function changeVerifyCode(img) {
	img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

/**
 * 根据key，返回value，例如https://www.nowcoder.com/discuss/256748?type=2&order=0&pos=20&page=1 ，传入page，返回1
 * @param name
 * @returns
 */
function getQueryString(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r =window.location.search.substr(1).match(reg);
	if(r != null){
		return decodeURIComponent(r[2]);
	}
	return '';
}