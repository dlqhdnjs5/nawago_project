
//이미지 파일 확장자 체크
//이미지 파일 일경우 true 리턴 그외 false 리턴
export function getFilExtCommon (file){
	
	if(file != null ){
		if(file.name != null){
			var acceptExt = ['jpg','jpeg','png'];
			
			var fileNm = file.name;
			var fileNmLength = fileNm.length;
			var lastDot = fileNm.lastIndexOf('.');
			
			
			var ext = fileNm.substring(lastDot+1, fileNmLength);
			ext = ext.toLowerCase();
			if(acceptExt.indexOf(ext) > -1){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	
	}else{
		return false;
	}

}

export function ImgfileSizeCheckCommon(v){
	if(v == null){
		return false;
	}else{
		if(v.size >= 10000000){ //10MB
			return false;
		}else{
			return true;
		}
	}
}