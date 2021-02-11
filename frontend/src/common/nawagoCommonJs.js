
//이미지 파일 확장자 체크
//이미지 파일 일경우 true 리턴 그외 false 리턴

//fileTp : IMG,MOV
export function checkFileExtCommon (file){
	
	if(file != null ){
		if(file.name != null){
			
			var fileTp = getFileTpCommon(file);
			
			var acceptExt = [];
			
			if(fileTp == 'IMG'){
				acceptExt = ['jpg','jpeg','png'];
			}else if(fileTp == 'MOV'){
				acceptExt =  ['webm','mov','ogg'];
			}else{
				return false;
			}
			
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

export function getImgFileExtCommon (file){
	
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

export function getMovFileExtCommon (file){
	
	if(file != null ){
		if(file.name != null){
			var acceptExt =  ['webm','mov','ogg'];
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

export function getFileTpCommon (file){
	
	if(file != null ){
		
		var imgExt = ['jpg','jpeg','png'];
		var movExt = ['webm','mov','ogg'];
		
		var fileUrl = file.name;
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