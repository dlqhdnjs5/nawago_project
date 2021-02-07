<template>
	<div class="text-center" >
		<v-layout  row wrap  class="fileDiv-wrap">
			<v-flex xs12 lg4 md4 xl3 
			style="padding-top : 50px; padding-bottom : 50px;"  
			v-for="(fileObj,idx) in fileList"
			>
				  <!-- 등록되엇을이미지 -->
				  <img
				    :id="'clientImg'+idx"
			        src=""
			        style=" max-width: 295px;	max-height: 295px; display : none;"
			      > 
			      
			      <!-- 등록용 이미지 -->
			      <img
			        :id="'plusImg'+idx"
			        src="@/assets/addContentPlus.png"
			        @click="fileClick(idx)"
			      >
			      <div 
			       :id="'deleteBtn' + idx" 
			       style="display:none;"
			      >
			      	<v-btn 
			      	 icon 
			      	 @click="deleteFile(idx)" 
			      	 >
				      	 <v-icon
					      color="teal darken-2"
					     >
					      mdi-trash-can-outline
					    </v-icon>
				    </v-btn>
			      </div> 
			      <!-- 파일 input -->
				  <input :id="'fileId' + idx" type="file"	@input="upload(idx)" style="display : none;"/> 
			</v-flex>
	     </v-layout>
	     <v-layout >
	     	<v-flex>
	     		<v-form
			    ref="form"
			    v-model="valid"
			    lazy-validation
			  	>
		     		 <v-textarea
		     		  v-model="showOffCont"
			          outlined
			          label="내용을 입력해주세요!	"
			          color ="#00BFA5"
			          height="250"
			          :rules = "showOffContRules"
			        ></v-textarea>
		       </v-form>
	     	</v-flex>
	     </v-layout>
	     <v-layout>
	     	<v-flex>
	     		<v-btn
			      class="mx-2"
			      fab
			      dark
			      large
			      color="#00BFA5"
			      @click="registShowOff"
			    >
			      <v-icon dark>
			        mdi-check-bold
			      </v-icon>
			    </v-btn>
	     	</v-flex>
	     </v-layout>
	</div>
</template>
<script>
import {getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import router from '@/router/index.js'
  export default {
    name: 'AddShowOff',
   	data : function(){
   		return {
   			valid: true,
   			show : false,
   			fileList :[], //파일 리스트이며 파일이 있고 없고 여부를 boolean 으로 넣는다.
   			files : [],
   			maxImgCount : 5, //파일 최대 갯수
   			showOffCont : '',
   			showOffContRules: [
				value => !!value && value.length < 200 || '내용은 최대 200자까지 입력가능합니다.',
				value =>  ( this.getIntroCheck(value)) || '특정문자(< , >)는 입력하실수 없습니다.',
		      ],
		    authAxios : getAuthCheckedAxios(),
   		}
   		
   	},
   	beforeCreate : function(){
   	},
   	created : function(){
   		var that = this;
   		that.fileList.push(false); //초기화
   		
   	},
   	methods : {
   		fileClick : function(idx){
   			var $fileEl = document.querySelector('#fileId'+idx);
   			$fileEl.click();
   		},
   		upload : function(idx) { //파일 업로드 이후
      	  	
   			var that = this;
			var $imgInput = document.querySelector('#fileId'+idx);
			
		    var fd = new FormData();
		    fd.append('data', $imgInput.files[0])
		    that.authAxios.post('/api/showOff/file/upload', fd,  
		    		  {
		    	  		headers: {
		    	    		'Content-Type': 'multipart/form-data'
		      			}
		    		  }
		      )
		    .then( response => {
		    	var $clientImg = document.querySelector('#clientImg'+idx);
		    	$clientImg.style.display=""; //클라이언트 이미지 영역 보이기
		    	$clientImg.setAttribute('src',response.data); //이미지 주소 넣기
		    	
		    	var $clientImg = document.querySelector('#plusImg'+idx);
		    	$clientImg.remove(); //플러스 이미지 삭제
		    	
		    	that.showDeleteBtn(idx);
		    	
		    	
		    	that.createBtn();
	         })
	          .catch(function (err) {
	            console.log('FAILURE!!');
	         });
			
        },
        showDeleteBtn : function(idx){
        	var $el = document.querySelector('#deleteBtn'+idx);
        	$el.style.display=''; //삭제 버튼 보이기
        },
   		check : function(){
   			var that = this;
   		},
		deleteFile : function(idx){
	 		var that = this;
	   	  	var $el = document.querySelector('#fileId'+idx).parentElement;
	   	  	var count = document.querySelector('.fileDiv-wrap').childElementCount;
	   	  
 	   	  $el.remove();
 	   	  that.createBtn();
	   	
	     },
	     getIntroCheck : function(value){
				if(value.includes('>') || value.includes('<')){
					return false;
				}else{
					return true;
				}
		 },
	     getRealFileName : function(fileUrl){//파일명 추출
				var fileLength = fileUrl.length;
			    var lastSlsh = fileUrl.lastIndexOf('/');
			    return  fileUrl.substring(lastSlsh+1, fileLength);
		 },
		 createBtn : function(){
       	  var that = this;
       	  var emptyFileCount = 0;
       	  var count = document.querySelector('.fileDiv-wrap').childElementCount;
       	  
		  var fileDivList = document.querySelector('.fileDiv-wrap').children;
			 
       	  
       	  for(var i = 0 ; i < fileDivList.length ; i ++){
       		  if(fileDivList[i].lastChild.files.length == 0)emptyFileCount++;
       	  }
       	  
       	  if(count < that.maxImgCount && emptyFileCount < 1) that.fileList.push(false); //that.fileList ++;
       	  
         },
         setAttchFileParam : function(){
        	
        	var that = this;
        	var $fileList = document.querySelector('.fileDiv-wrap').children;
        	var attchFileList = [];
        	
        	for(var i = 0 ; i < $fileList.length ; i++){
        		var $fileEl = $fileList[i];
        		
        		//자식 엘리먼트가 4보다 작다는것은 사용자가 파일을 첨부했다는뜻
        		//자식 엘리먼트가 3보다  크다는것은 파일을 첨부하기 전이라는 뜻
        		if($fileEl.children.length < 4){
        			attchFileList.push($fileEl.firstChild.src);
        		}
        	}
        	
        	return attchFileList;
         },
         registShowOff : function(){
        	 if(!this.$refs.form.validate())return;
        	 
        	 var that = this;
        	 var param = {
        			'fileAttachList' : that.setAttchFileParam(),
        			'showOffjpa' : {
        							'showOffCont' : that.showOffCont
        							}
        	 }
        	 
        	 that.authAxios.post('/api/showOff/add',param)
        	 .then(function(resp){
        		var that = this;
        		router.push({
        			name : 'showOffList'
        		})
        	 })
        	 .catch(function(err,status){
        		console.log(err); 
        		console.log('시스템 오류가 발생하였습니다.'); 
        	 });
         }
   	}
  }
</script>
