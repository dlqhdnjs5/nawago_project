
//이미지 파일 확장자 체크
//이미지 파일 일경우 true 리턴 그외 false 리턴
export function getFilExtCommon (file){
	
	if(file != null ){
		if(file.name != null){
			
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

export function getFileTpCommon (fileUrl){
	
	if(fileUrl != null ){
		
		var imgExt = ['jpg','jpeg','png'];
		var movExt = ['webm','mp4','ogg'];
		
		var fileUrlLength = fileUrl.length;
		var lastDot = fileUrl.lastIndexOf('.');
		var ext = fileUrl.substring(lastDot+1, fileUrlLength);
		ext = ext.toLowerCase();
		
		if(imgExt.indexOf(ext) > -1 ){
			return 'IMG';
		}else if( movExt.indexOf(ext) > -1  ) {
			return 'MOV';
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