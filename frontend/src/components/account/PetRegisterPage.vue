<template>
	<v-container >
	<div class="text-center">
	 	<!-- <v-layout> -->
	 		<!-- <v-flex xs12 lg12 md12 xl12  > -->
	 		<v-form
			    ref="form"
			    v-model="valid"
			    lazy-validation
			  >
			<v-layout >
				<v-flex xs12 lg12 md12 xl12>
				
					<v-badge
				            icon="mdi-camera-flip"
					        bordered
					        bottom
					        overlap
					        offset-x="30"
		        			offset-y="20"
					        color="#00BFA5"
				    >
				 		<v-avatar size="89">
						      <img v-if="(petImgUrl == null  && petImgNm == null ) || ( petImgNm == '' && petImgUrl == '') "
						        src="@/assets/emptyPetProfile.png"
						        @click="sheet = !sheet"
						      >
						      <img v-else
						        :src="petImgUrl + '/' + petImgNm"
						        @click="sheet = !sheet"
						      >
						</v-avatar>
					</v-badge>
					<input id="fileId" type="file"	@input="upload" style="display : none;"/>
				</v-flex>
			</v-layout>
			<v-layout >
				<v-flex >
			 		<v-text-field
		 				v-model="petNm"
		 				id="petNm"
		 				:rules="petNmRules"
		 				color="#00BFA5"
		 				label ="*이름">
			 		</v-text-field>
		 		</v-flex>
	 		</v-layout>
	 		<v-layout >
				<v-flex xs12 lg12 md12 xl12 >
			 		<v-radio-group
		              v-model="petSex"
		              row
		            >
			 		 <v-radio
		                label="여자"
		                color="#F48FB1"
		                value="FEMALE"
		              ></v-radio>
		              <v-radio
		                label="남자"
		                color="#81D4FA"
		                value="MALE"
		              ></v-radio>
		            
		             </v-radio-group>
				</v-flex>	
	          </v-layout>
             </br>
             <v-layout>
             	<span>*성향</span>
             </v-layout>
              <div v-if="isCharErr" class=" theme--light error--text caption" style="color:red;">
             	<span>성향을 선택해주세요</span>
             </div>
            
             </br>
             <v-chip-group v-if="petCharChipList != null"
		        v-model="petChar"
		        active-class="#F48FB1"
		        column
		        multiple
		      >
		        <v-chip v-for="petCharChip in petCharChipList"
		          :key="petCharChip.cd"
		          color="#F48FB1" 
		          text-color="#F48FB1"
		          filter
		          outlined
		          
		        >
		          {{petCharChip.cdNm}}
		        </v-chip>
		     </v-chip-group>
             <br>
	 		<v-switch
              v-model="petBirthYn"
              label="생년월일 사용"
              color="#00BFA5"
              hide-details
            ></v-switch>
		 	</br>
	        <v-row justify="space-around">
		        <v-date-picker v-if="petBirthYn"
			      v-model="petBirth"
			      color="#00BFA5"
			    ></v-date-picker>
		    </v-row>
	 		<v-textarea
	          outline
	          color="#00BFA5"
	          v-model="petIntro"
	          :rules="petIntroRules"
	          label="*소개글을 남겨주세요!"
	        ></v-textarea>
	        <v-btn
		      class="mx-2"
		      fab
		      dark
		      large
		      color="#00BFA5"
		      :disabled="!valid"
		      @click="registPet"
		    >
		      <v-icon dark>
		        mdi-check-bold
		      </v-icon>
		    </v-btn>
	 		</v-form>
	      	<!-- <p>File Name : {{ files.name }}</p> -->
	 		<!-- </v-flex> -->
	 	<!-- </v-layout> -->

		<div class="text-center">
		    <v-bottom-sheet v-model="sheet">
		      <v-list>
		        <v-subheader>프로필</v-subheader>
		        <v-list-item
		          @click="sheet = false; fileClick();"
		        >
		          <v-list-item-avatar>
			            <v-avatar color="#00BFA5">
					      <v-icon dark>
					        mdi-cat
					      </v-icon>
					    </v-avatar>
		          </v-list-item-avatar>
		          <v-list-item-title>식구 사진 바꾸기</v-list-item-title>
		        </v-list-item>
		        <v-list-item
		          @click="sheet = false ; delPetImg();"
		        >
		          <v-list-item-avatar>
		            <v-avatar color="#F48FB1">
				      <v-icon dark>
				        mdi-close-thick
				      </v-icon>
				    </v-avatar>
		          </v-list-item-avatar>
		          <v-list-item-title>현재 사진 지우기</v-list-item-title>
		        </v-list-item>
		      </v-list>
		    </v-bottom-sheet>
		</div>
	</div>
	</v-container>
</template>

<script>
import router from '@/router/index.js'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'
import {getFilExtCommon , ImgfileSizeCheckCommon} from '@/common/nawagoCommonJs'

  export default {
    name: 'RegisterPage',
	data : function() {
		return {
			sheet: false,
			valid: true,
			files: [],
			petImgUrl : null,
			petTest : null,
			petImgNm : null,
		    imgRules: [
					value =>  this.fileCheck(value) || '이미지를 등록해주세요.',
					value =>  this.fileSizeCheck(value) || '이미지는 2MB 를 넘을수 없습니다.',
					value =>  this.getFilExt(value) || 'jpeg,jpg,png 만 등록 할수있습니다.',
			      ],
		    petNmRules: [
		        value => !!value || '이름을 입력해 주세요.',
		        value => (value && value.length <= 15) || '이름은 최대 15자까지 입력 가능합니다.',
		        value => this.checkEx(value) || '특수문자나 공백을 제외하고 입력해 주세요.'
		      ],
	      	petIntroRules: [
		        value => !!value && value.length >= 10 || '10자 이상의 소개글을 입려해주세요.',
		        value => (value && value.length <= 100) || '이름은 최대 100자까지 입력 가능합니다.',
		        value => ( this.getIntroCheck(value)) || '특정문자(< , >)는 입력하실수 없습니다.',
		      ],
		    petNm  : null,
		    petSex : 'FEMALE',
		    petChar: [],//선택된 성격목록
		    petCharChipList : [],//성격목록
		    petIntro : '',
		    petSpec : null, //종(사용안함)
		    petBirth: new Date().toISOString().substr(0, 10),
		    petBirthYn : true,
		    isCharErr : false,
		}
	},
	methods : {
		fileClick : function(idx){
   			var $fileEl = document.querySelector('#fileId');
   			$fileEl.click();
   		},
		checkMbrIdVd : function(id){
			var regType1 = /^[A-Za-z0-9+]*$/
			return regType1.test(id);
		},
		checkEx : function(value){
			var that = this;
			var pattern_spc = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자
			
			return !that.checkBlank(value) && !pattern_spc.test(value);
		},
		checkBlank : function(value){
			var blank_pattern = /[\s]/g;
			return blank_pattern.test(value);
		},
		upload : async  function(){ //파일 업로드

			var that = this;
		    var fd = new FormData();
		    var $imgInput = document.querySelector('#fileId')
		    
		    if(!getFilExtCommon($imgInput.files[0])){
				alert('이미지 파일만 등록 가능합니다.');
				return ;
			}
		    
		    if(!ImgfileSizeCheckCommon($imgInput.files[0])){
		    	alert('이미지는 최대 10MB 까지 등록 가능합니다.');
				return ;
		    }
		    
		    fd.append('data',  $imgInput.files[0])
		    
		    await axios.post('/api/pet/file/upload', fd,  
		    		  {
		    	  		headers: {
		    	    		'Content-Type': 'multipart/form-data'
		      			}
		    		  }
		      )
		    .then( response => {
		            console.log('SUCCESS!!');
		            var result = response.data;
		            that.petImgNm = that.getRealFileName(result);
		            that.petImgUrl = that.getPetImgUrl(result);
		           
	          })
	          .catch(function () {
	            console.log('FAILURE!!');
	          });
		    },
		    delPetImg : async function(){//파일 삭제
		    	
		    	var $imgInput = document.querySelector('#fileId');
		    	if($imgInput.files.length < 1)return;
		    	var that = this;
		    	var param = {
						fileName: that.petImgNm
				}
				await axios.post('/api/pet/file/delete', param)
			      .then( response => {
			            console.log('SUCCESS DELETE!!');
		          })
		          .catch(function () {
		            	console.log('FAILURE DELETE!!');
		          });
		    	that.petImgNm = null;
	            that.petImgUrl = null;
	            document.querySelector('#fileId').value=null;
			},
			getRealFileName : function(fileUrl){//파일명 추출
				
				var fileLength = fileUrl.length;
			    var lastSlsh = fileUrl.lastIndexOf('/');
			    return  fileUrl.substring(lastSlsh+1, fileLength);
			},
			getPetChar : function(){
				
				var that = this;
				var charList = '';
				that.petChar.forEach($elementIdx=>{
					var $selectedChip = that.petCharChipList[$elementIdx];
					charList += ( charList == '' ?  $selectedChip.cdNm : ','+ $selectedChip.cdNm);
				});
				
				return charList;
			},
			registPet : function(){
				var that = this;
				
				if(  this.$refs.form.validate() && that.checkPetChar()  ){
					
					console.log('param : '+that.setParam());
					var param = {};
					var petJpa = that.setParam();
					param['petJpa'] = petJpa;
					var authAxios = getAuthAxios();
					authAxios.post('/api/pet/registPet',param)
					.then(resp =>{
						alert('등록을 완료하였습니다');
						this.$router.push({
							name : 'myPage'
						})
					})
					.catch(err => {
						alert('실패')
					});
					
					
				}
			},
			checkPetChar : function(){
				var that = this;
				if(that.petChar.length < 1){
					that.isCharErr = true;
					return false;
				}else{
					that.isCharErr = false;
					return true;
				}
			},
			fileCheck : function(v){
				console.log(v)
				if(v == null){
					return false;
				}else{
					return true;
				}
			},
			fileSizeCheck : function(v){
				if(v == null){
					return false;
				}else{
					if(v.size >= 2000000){
						return false;
					}else{
						return true;
					}
				}
			},
			setParam : function(){
				var that = this;
				var param = {
					'petNm' 	: that.petNm,
					'petSex' 	: that.petSex,
					'petCharCd'	: that.getPetChar(),
					'petBirth' 	: that.petBirthYn ? that.petBirth : null,
					'petIntro'	: that.petIntro,
					'petImgUrl'	: that.petImgUrl, 
					'petImgNm'	: that.petImgNm
				}
				
				return param;
			},
			getPetImgUrl : function(fullUrl){
				var lastSlsh = fullUrl.lastIndexOf('/');
				return fullUrl.substring(0,lastSlsh);
			},
			getIntroCheck : function(value){
				if(value.includes('>') || value.includes('<')){
					return false;
				}else{
					return true;
				}
			}
			
	},
	created : function(){
		var that = this;
		this.$store.dispatch('getCommonCd','PET_CHAR')
		.then(resp =>{
			that.petCharChipList = resp;
		})
		.catch(err => {
			console.log("Can't get Common sysCd!");
		})
		
	},
	mounted : function(){
		 
		 
	},
	updated : function(){
		
	}
	
}
</script>
