<template>
	<div >
		<div>
			<v-layout align-right class="text-center">
				<v-flex xs12 lg12 md12 xl12 
					v-if="isOwner"
				>
					<v-badge
			            icon="mdi-bone"
				        bordered
				        bottom
				        overlap
				        offset-x="30"
	        			offset-y="20"
				        color="#00BFA5"
			    	>
				 		<v-avatar size="89">
						      <img v-if="(petInfo.petImgUrl == null  && petInfo.petImgNm == null ) || ( petInfo.petImgNm == '' && petInfo.petImgUrl == '') "
						        src="@/assets/emptyPetProfile.png"
						        @click="sheet = !sheet"
						      >
						      <img v-else
						        :src="petInfo.petImgUrl + '/' + petInfo.petImgNm"
						        @click="sheet = !sheet"
						      >
						      <input id="fileId" type="file"	@input="upload" style="display : none;"/>
						</v-avatar>
					</v-badge>
				</v-flex>
				<v-flex xs12 lg12 md12 xl12
					v-else
				>
			 		<v-avatar size="89">
					      <img v-if="(petInfo.petImgUrl == null  && petInfo.petImgNm == null ) || ( petInfo.petImgNm == '' &&petInfo.petImgUrl == '') "
					        src="@/assets/emptyPetProfile.png"
					      >
					      <img v-else
					        :src="petInfo.petImgUrl + '/' + petInfo.petImgNm"
					      >
					</v-avatar>
				</v-flex>
				<v-flex>
				</v-flex>
			</v-layout>
			<v-layout class="text-center">
				<v-flex>
					<br>
					<span class="font-weight-bold" v-text="petInfo.petNm" ></span>
					<v-icon  color="pink" v-if="petInfo.petSex == 'FEMALE' ">mdi-gender-female</v-icon>
					<v-icon  color="blue" v-else>mdi-gender-male</v-icon>
				</v-flex>
			</v-layout>
			<v-layout class="text-center">
				<v-flex >
					<span class="text-caption" v-text="getAge(petInfo.petBirth)"></span>&nbsp
					<v-icon color="#F48FB1" v-if="isBaby">
						mdi-heart-multiple
					</v-icon>
				</v-flex>
			</v-layout>
			<v-layout style="padding-top : 30px;" class="text-center">
				<v-flex class="font-weight-bold text--disabled">
					성향
				</v-flex>
			</v-layout>
			<v-layout class="text-center">
				<v-flex>
					<v-chip
					v-for="Char in petCharList"
					class="ma-2"
					color="#90CAF9"
					dark
				    >
				      {{Char}}
				    </v-chip>
			    </v-flex>
			</v-layout>
			<v-layout style="padding-top : 40px;" class="text-center">
				<v-flex class="font-weight-bold text--disabled">
					인삿말
				</v-flex>
			</v-layout>
			<v-layout >
			<div  class="text-caption"  style="width:100%; overflow:hidden; word-wrap:break-word;padding-bottom:10%;padding-left:10%;padding-right:10%;padding-top:3%;" v-html="getShoOffCont(petInfo.petIntro)"  >
				
			</div>
			</v-layout>
			
			
		
		</div>
		<div>
			<div >
				<v-bottom-sheet v-model="sheet">
					<v-list>
						<v-subheader>프로필</v-subheader>
						<v-list-item
								@click="$router.push({
									name : 'updatePet',
									query : {
										petSeq : petInfo.petSeq
									}
								})"
							>
							<v-list-item-avatar>
								<v-avatar color="indigo">
									<v-icon dark>
									mdi-dog
									</v-icon>
								</v-avatar>
							</v-list-item-avatar>
							<v-list-item-title>식구 정보 바꾸기</v-list-item-title>
						</v-list-item>
						<v-list-item
								@click="sheet = false; fileClick();"
							>
							<v-list-item-avatar>
								<v-avatar color="indigo">
									<v-icon dark>
									mdi-cat
									</v-icon>
								</v-avatar>
							</v-list-item-avatar>
							<v-list-item-title>식구 프로필 사진 바꾸기</v-list-item-title>
						</v-list-item>
						<v-list-item
							@click="sheet = false ; updateProfilePhoto('');"
						>
							<v-list-item-avatar>
								<v-avatar color="indigo">
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
	</div>
</template>

<script>
import router from '@/router/index.js'
import {getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import {getFilExtCommon , ImgfileSizeCheckCommon} from '@/common/nawagoCommonJs'

  export default {
    name: 'PetDetail',
	data : function() {
		return {
			petSeq : this.$route.params.petSeq,
			userId : this.$route.params.userId,
			sheet: false,
			valid: true,
			files: [],
			authAxios : getAuthCheckedAxios(),
			isOwner : false,
			petInfo : {
				petImgUrl : null,
				petTest : null,
				petImgNm : null,
			    petNm  : null,
			    petSex : null,
			    petCharCd : null,
			    petIntro : '',
			    petSpec : null, //종(사용안함)
			    petBirth: null,
			    petBirthYn : null,
			    isCharErr : false,
			    petSeq : null
			},
			petCharList : [],
		}
	},
	beforeCreate(){
		this.$store.commit('getPublicMbrInfo')
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
		
		that.getPetInfo();
		
		
	},
	methods : {
		getPetInfo : function(){
			var that = this;
			
			axios.get('/api/pet/getPetInfo',{
				params : {
					'petSeq' : that.petSeq 
				}
			})
			.then(function(resp){
				that.petInfo = resp.data;
				var mbrSeq = resp.data.mbrPetMappingJpa.mbrSeq;
				if(that.$store.getters.getIsLogin){
					if(mbrSeq == that.$store.getters.getMbrSeq ){
						that.isOwner = true;
					}
				}
				that.petCharList = that.petInfo.petCharCd.split(',');
				
			})
			.catch(function(err){
				console.log(err);
			})
		},
		getAge : function(birth){
			var birthday = new Date(birth);
			var today = new Date();
			var years = today.getFullYear() - birthday.getFullYear();
			var month = today.getMonth() - birthday.getMonth()
			if(years == 0){
				return month+' 개월';
			}else{
				return years+'살 '+month+' 개월'
			}
		},
		isBaby : function(birth){
			var birthday = new Date(birth);
			var today = new Date();
			var years = today.getFullYear() - birthday.getFullYear();
			
			return years < 1 ? true : false;
		},
		fileClick : function(idx){
   			var $fileEl = document.querySelector('#fileId');
   			$fileEl.click();
   		},
		upload : async  function(){ //파일 업로드

			var that = this;
		   
		    var $imgInput = document.querySelector('#fileId')
		    if(!getFilExtCommon($imgInput.files[0])){
				alert('이미지 파일만 등록 가능합니다.');
				return ;
			}
		    
		    if(!ImgfileSizeCheckCommon($imgInput.files[0])){
		    	alert('이미지는 최대 10MB 까지 등록 가능합니다.');
				return ;
		    }
		    
		    var fd = new FormData();
		    fd.append('data',  $imgInput.files[0])
		    await axios.post('/api/pet/file/upload', fd,  
		    		  {
		    	  		headers: {
		    	    		'Content-Type': 'multipart/form-data'
		      			}
		    		  }
		      )
		    .then( response => {
		            var result = response.data;
		            console.log(result)
		            that.updateProfilePhoto(result);
		           /*  that.petInfo.petImgNm = that.getRealFileName(result);
		            that.petInfo.petImgUrl = that.getPetImgUrl(result); */
		           
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
						fileName: that.petInfo.petImgNm
				}
		    	
				await axios.post('/api/pet/file/delete', param)
			      .then( response => {
		          })
		          .catch(function () {
		            	console.log('FAILURE DELETE!!');
		          });
		    	that.petInfo.petImgNm = null;
	            that.petInfo.petImgUrl = null;
	            document.querySelector('#fileId').value=null;
		},
		getRealFileName : function(fileUrl){//파일명 추출
				
				var fileLength = fileUrl.length;
			    var lastSlsh = fileUrl.lastIndexOf('/');
			    return  fileUrl.substring(lastSlsh+1, fileLength);
			},
			getPetImgUrl : function(fullUrl){
				var lastSlsh = fullUrl.lastIndexOf('/');
				return fullUrl.substring(0,lastSlsh);
		},
		updateProfilePhoto : function(imgUrl){
	        	
	        	var that = this;
	        	var param = that.setProfilePhotoParam(imgUrl);
	        	
	        	
	        	that.authAxios.post('/api/pet/update/petProfilePhoto',param )
			    .then( response => {
			    	that.getPetInfo();
		         })
		          .catch(function (err) {
		        	alert('시스템 오류가 발생하였습니다.')
		            console.log('fail update profile');
		         });
	    },
	    setProfilePhotoParam : function(imgUrl) {
	        	var that = this;
	        	var param = {
	        		'petSeq' :	that.petInfo.petSeq,
	        		'attachFileUrl' : imgUrl
	        	}
	        	return param;
	     },
	     getShoOffCont : function(shoOffCont){
				return shoOffCont.replaceAll('\n','</br>');
	   	}
			
	},
}
</script>
