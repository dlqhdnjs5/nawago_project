<template>
	<div  v-if="myInfo != null" >
		<v-layout row wrap class="text-center">
			<v-flex xs12 v-if="isMySelf" >
				<v-badge
		            icon="mdi-emoticon"
			        bordered
			        bottom
			        overlap
			        offset-x="30"
        			offset-y="20"
			        color="#00BFA5"
		        >
				<v-avatar size="110">
			      <img v-if="myInfo.mbrRpstImgUrl != null &&  myInfo.mbrRpstImgNm != null"
			        :src="myInfo.mbrRpstImgUrl + '/' + myInfo.mbrRpstImgNm"
			        id="clientImg"
			        @click="sheet = !sheet"
			      >
			      <img v-else
			        src="@/assets/emptyProfile2.png"
			        id="emptyImg"
			        @click="sheet = !sheet"
			      >
			      <input id="fileId" type="file"	@input="upload" style="display : none;"/>
			    </v-avatar>
			    </v-badge>
			   <div>
				 <br>
		      	 <v-progress-circular
		      	  v-if="progress"
			      :size="30"
			      :width="7"
			      color="#F48FB1"
			      indeterminate
			    
				  ></v-progress-circular>
		      </div>
			</v-flex>
			<v-flex xs12 v-else >
				<v-avatar size="110">
			      <img v-if="myInfo.mbrRpstImgUrl != null &&  myInfo.mbrRpstImgNm != null"
			        :src="myInfo.mbrRpstImgUrl + '/' + myInfo.mbrRpstImgNm"
			        id="clientImg"
			      >
			      <img v-else
			        src="@/assets/emptyProfile2.png"
			        id="emptyImg"
			      >
			    </v-avatar>
			</v-flex> 
			<v-flex xs12 >
				<div>
					<span>{{myInfo.mbrNickNm}}</span><br>
					<p class="grey--text">{{myInfo.mbrId}}</p>
				</div>
			</v-flex>
		</v-layout> 
		
		
		<v-divider style="padding-top : 40px;">
		</v-divider>
		
		
		 <v-layout row wrap class="text-center">
			  <v-expansion-panels >
			      <v-expansion-panel>
			        <v-expansion-panel-header v-if="isMySelf">나의 식구</v-expansion-panel-header>
			        <v-expansion-panel-header v-else>식구</v-expansion-panel-header>
			        <v-expansion-panel-content>
				        <v-layout row wrap v-if="isMySelf && (petList == null || petList.length < 1)">
							<v-flex  >
								<v-btn block  depressed color="primary"
					 				@click="$router.push({
					 				name : 'PetRegisterPage'
					 				})">
					 				식구를 등록해주세요!
					 			</v-btn>
							</v-flex>
						</v-layout>
						<v-layout row wrap v-else-if="petList != null && petList.length > 0" >
							<v-flex xs6 lg4 md4 xl12
								v-for="pet in petList">
								<v-badge
									icon="mdi-gender-female"
									bordered
									overlap
									offset-x="20"
				        			offset-y="20"
									color="pink"
									v-if="pet.petSex == 'FEMALE' "
						        >
									<v-avatar size="105">
										<img v-if="pet.petImgUrl != null && pet.petImgNm != null"
										:src="pet.petImgUrl + '/' + pet.petImgNm"
										alt="pet.petNm"
										  @click="$router.push({
											path: '/pet/'+userId+'/'+pet.petSeq
										})""
										>
										<img v-else
										src="@/assets/emptyPetProfile.png"
										@click="$router.push({
											path: '/pet/'+myInfo.mbrId+'/'+pet.petSeq
										})"
										>
									</v-avatar>
							    </v-badge>
							    <v-badge
						            icon="mdi-gender-male"
							        bordered
							        overlap
							        offset-x="20"
				        			offset-y="20"
							        v-else-if="pet.petSex == 'MALE'"
						        >
									<v-avatar size="105">
									<img v-if="pet.petImgUrl != null && pet.petImgNm != null"
								        :src="pet.petImgUrl + '/' + pet.petImgNm"
								        alt="pet.petNm"
								          @click="$router.push({
											path: '/pet/'+userId+'/'+pet.petSeq
										})"
									>
									<img v-else
										src="@/assets/emptyPetProfile.png"
										@click="$router.push({
											path: '/pet/'+myInfo.mbrId+'/'+pet.petSeq
										})"
									>
								    </v-avatar>
							    </v-badge>
							    <br>
							    <div style="padding-top : 8px;">
							    	<div><span class=" text-caption" >{{pet.petNm}}</span></div>
							    	<div><span class=" text-caption" >{{pet.petCharCd}}</span></div>
							    </div>
							    <br>
							    <br>
							</v-flex >
							<v-flex style="padding-top : 20px; " v-if="isMySelf">
							 <button id="plusBtn" 
							 	@click="$router.push({
							 		name : 'PetRegisterPage'
								 })">
					     		<v-avatar
									color="teal"
									size="48"
							    >
							     <span class="white--text headline">+</span>
							    </v-avatar>
							  </button>
					     	</v-flex>
						</v-layout>
						<v-layout row wrap v-else>
							<v-flex>
								비어 있음
							</v-flex>
						</v-layout>
			        </v-expansion-panel-content>
			      </v-expansion-panel>
			    </v-expansion-panels>
		</v-layout> 
		
		
		<v-layout
			style="padding-top : 40px;" 
		>
			<v-flex>
				<div style="padding-bottom : 10px;">
					<span v-if="isMySelf">나의 스토리</span>
					<span v-else>스토리</span>
				</div>
				<v-divider>
				</v-divider>
			</v-flex> 
		</v-layout>
		
		<v-layout 
			style="padding-top : 40px;" 
			v-if="myShowOffList != null && myShowOffList.length > 0"
			row wrap
		>
			<v-flex xs12 sm4 md4 lg4 xl4
				v-for="myShowOffObj in myShowOffList"
			>
				<v-card
					class="mx-auto"
					max-width="344"
				  >
					<template v-if="myShowOffObj.showOffJpa.showOffAttachJpa.length > 1">
						<swiper ref="mySwiper" 
							class="swiper"
							:options="swiperOptions" 
							v-if="myShowOffObj.showOffJpa.showOffAttachJpa != null" 
						> 
							<swiper-slide v-for="(showOffAttachObj, conttIdx ) in myShowOffObj.showOffJpa.showOffAttachJpa" >
								<template v-if="showOffAttachObj.showOffAttachTpCd == 'IMG'">
									<v-img 
										class="white--text align-end"
										height="295"
										:src="showOffAttachObj.showOffAttachUrl + '/' + showOffAttachObj.showOffAttachNm"
									></v-img>
								</template>
								<template v-else >
									<video
								      	:id="'clientMov'+conttIdx"
								      	class="video-js vjs-default-skin vjs-big-play-centerd" 
								      	playsinline
								      	style="margin:0 auto;height:295px"
								      	preload="metadata" 
								      	controls
								      	:src="showOffAttachObj.showOffAttachUrl + '/' + showOffAttachObj.showOffAttachNm +'#t=0.5'"
							      	>
							      	</video>
							      	<!-- autoplay  -->
							      	<!-- muted="muted" -->
							      	<!-- loop  -->
								
								</template>
							</swiper-slide>
							 <div class="swiper-pagination s01" slot="pagination"></div>
						</swiper>
					</template>
					<template  v-else-if="myShowOffObj.showOffJpa.showOffAttachJpa.length == 1">
						<template v-if="myShowOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachTpCd == 'IMG'">
							<v-img 
							class="white--text align-end"
							height="295"
							:src="myShowOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachUrl + '/' + myShowOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachNm"
							></v-img>
						</template>
						<template v-else>
							<video
						      	class="video-js vjs-default-skin vjs-big-play-centerd" 
						      	playsinline
						      	style="margin:0 auto;height:295px;width:344px;"
						      	controls
						      	preload="metadata"
						      	:src="myShowOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachUrl + '/' + myShowOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachNm +'#t=0.5'"
					      	>
					      	</video>
						</template>
						
					</template>
					
						<!-- <v-card-title>Top 10 Australian beaches</v-card-title> -->
					
					<v-card-subtitle 
						class="pb-0"
						v-text="timeForToday(myShowOffObj.showOffJpa.regDt)"
					>
					</v-card-subtitle>
				
					<v-card-text class="text--primary"
						v-html="getShoOffCont(myShowOffObj.showOffJpa.showOffCont)"
					>
					</v-card-text>
				
					<v-card-actions>
						<v-btn
							v-if="myShowOffObj.myLike == '1'"
					        color="#F48FB1"
					        icon
					        :data-btn-click="setPushed(myShowOffObj.myLike)"
					        @click="updateShowOffLike(myShowOffObj.showOffJpa.showOffSeq, myShowOffObj.myLike, $event)"
						>
							<!-- <v-icon  v-if="myShowOffObj.myLike == '1'">mdi-cards-heart</v-icon> -->	<!-- mdi-bone -->
					        <v-icon>mdi-heart-outline</v-icon>	
					        <span>{{myShowOffObj.likeCnt}}</span>
						</v-btn>
						<v-btn
							v-else
					        color=""
					        icon
					        :data-btn-click="setPushed(myShowOffObj.myLike)"
					        @click="updateShowOffLike(myShowOffObj.showOffJpa.showOffSeq, myShowOffObj.myLike, $event)"
						>
							<!-- <v-icon  v-if="myShowOffObj.myLike == '1'">mdi-cards-heart</v-icon> -->	<!-- mdi-bone -->
					        <v-icon>mdi-heart-outline</v-icon>	
					        <span>{{myShowOffObj.likeCnt}}</span>
						</v-btn>
						
						
						<v-btn
					        color="#00BFA5"
					        icon
					        @click="replySheet = !replySheet; openReply(myShowOffObj.showOffJpa.showOffSeq);"
						>
							<v-icon>mdi-chat-outline</v-icon>	
							<span :id="'showOffreplyCnt' + myShowOffObj.showOffJpa.showOffSeq" >{{myShowOffObj.replyCnt}}</span>
						</v-btn>
						<div >
							<v-btn
							    color="grey"
						        icon
						        @click="menuSheet = !menuSheet; setMenuShowOffSeq(myShowOffObj.showOffJpa.showOffSeq)"
							>
						        <v-icon>mdi-menu</v-icon>	
							</v-btn>
						</div>
					</v-card-actions>
				</v-card>
			</v-flex>
		</v-layout>
		<div class="text-center">
			<v-layout v-if="loading" xs12 sm12 md12 lg12 xl12 class="text-center">
				<v-flex>
					<v-progress-circular
				      indeterminate
				      color="#00BFA5"
				    ></v-progress-circular>
				</v-flex>
			</v-layout>
		</div>
		
		<div class="text-center">
		    <v-bottom-sheet v-model="sheet">
		      <v-list>
		        <v-subheader>프로필</v-subheader>
		        <v-list-item 
		          @click="sheet = false; updateMbr();"
		        >
		          <v-list-item-avatar>
			            <v-avatar color="#00BFA5">
					      <v-icon dark>
					        mdi-account-convert
					      </v-icon>
					    </v-avatar>
		          </v-list-item-avatar>
		          <v-list-item-title>회원 정보 바꾸기</v-list-item-title>
		        </v-list-item>
		        <v-list-item
		          @click="sheet = false; fileClick();"
		        >
		          <v-list-item-avatar>
			            <v-avatar color="#00BFA5">
					      <v-icon dark>
					        mdi-camera
					      </v-icon>
					    </v-avatar>
		          </v-list-item-avatar>
		          <v-list-item-title>프로필 사진 바꾸기</v-list-item-title>
		        </v-list-item>
		        <v-list-item
		          @click="sheet = false ; updateProfilePhoto('');"
		        >
		          <v-list-item-avatar>
		            <v-avatar color="#F48FB1">
				      <v-icon dark>
				       mdi-camera-off
				      </v-icon>
				    </v-avatar>
		          </v-list-item-avatar>
		          <v-list-item-title>현재 사진 지우기</v-list-item-title>
		        </v-list-item>
		      </v-list>
		    </v-bottom-sheet>
		  </div> 
	
	
	
	<div class="text-center">
		<v-bottom-sheet
		v-model="replySheet"
		inset
		>
		<template v-slot:activator="{ on, attrs }">
		</template>
		<v-sheet
			height="1000px"
		>
			<div align="right">
				<v-btn
					align="right"
					class="mt-6"
					text
					icon
					@click="replySheet = !replySheet "
					color="red lighten-2"
					style="padding-bottom : 20px;"
				>
					<v-icon>mdi-close-thick</v-icon>
				</v-btn>
			</div>
			<v-divider style="padding-top : 40px;">
			</v-divider>
			<div
				style="bottom: 0;width:90%;"
				class="text-center"
			>
				<v-layout style="padding-left:7%">
					<v-flex row wrap>
							<v-avatar size="38">
							<template v-if="$store.getters.getIsLogin">
						      <img v-if="$store.getters.getMbrImg != null"
					      		:src="$store.getters.getMbrImg"
					      	  >
					      	   <img v-else
					      		src="@/assets/emptyProfile2.png"
					      	  >
					      	</template>
						    </v-avatar>
							<v-text-field v-if="$store.getters.getIsLogin"
								label="댓글달기"
								outlined
								dense
								color="#00BFA5"
								style="padding-left:2%;"
								v-model="replyCont"
							>
							</v-text-field>
							<v-text-field v-else	
								label="로그인을 해주세요!"
								outlined
								dense
								style="padding-left:2%;"
								v-model="replyCont"
								disabled="disabled"
								@click="askLogin"
							>
							</v-text-field>
						
							<v-btn
								id="addReplyBtn"
								style="padding-left:9%;"
								icon
								color="#00BFA5"
								@click="addShowOffReply()"
								:disabled="btnDisabled"
							>
							 	<v-icon large >mdi-send</v-icon>
							</v-btn>
					</v-flex>
				</v-layout>
				
			</div>
			<div
				style="overflow:scroll;height:500px;"
			>
				<v-layout
					column
				>
					<v-flex>
						<v-list-item  v-for="showOffReply in showOffReplyList"> 
					      <v-list-item-avatar color="grey">
					      	<img 
					      		v-if="showOffReply.mbrJpa.mbrRpstImgUrl != null && showOffReply.mbrJpa.mbrRpstImgNm != null"
					      		:src="showOffReply.mbrJpa.mbrRpstImgUrl + '/' + showOffReply.mbrJpa.mbrRpstImgNm"
					      	>
					      	<img 
					      		v-else
					      		src="@/assets/emptyProfile2.png"
					      	>
					      </v-list-item-avatar>
					      <v-list-item-content>
					        <v-list-item-title v-text="showOffReply.mbrJpa.mbrNickNm"></v-list-item-title>
					        <v-list-item-subtitle v-text="timeForToday(showOffReply.regDt)"></v-list-item-subtitle> 
					        <span v-html="showOffReply.replyCont">
					        </span>
					      </v-list-item-content>
					    </v-list-item> 
					</v-flex>
				</v-layout>
			</div>
			</v-sheet>
		</v-bottom-sheet>
	</div>
	
	
	<!-- 스토리 메뉴 -->
	<div >
		<v-bottom-sheet v-model="menuSheet">
			<v-list>
				<v-subheader>스토리 메뉴</v-subheader>
				<v-list-item 
						v-if="isMySelf"
						@click=""
					>
					<v-list-item-avatar>
						<v-avatar color="#F48FB1">
							<v-icon dark>
							mdi-delete-empty
							</v-icon>
						</v-avatar>
					</v-list-item-avatar>
					<v-list-item-title>스토리 지우기(준비중)</v-list-item-title>
				</v-list-item>
			</v-list>
		</v-bottom-sheet>
	</div>
	
	
	<!-- <v-speed-dial 나중에 사용할것
      v-model="fab"
      :bottom="true"
      :right="true"
      :direction="'top'"
      :open-on-hover="false"
      :transition="transition"
      absolute
      fixed
      style="padding-bottom:20%;"
    >
      <template v-slot:activator>
        <v-btn
          v-model="fab"
          color="blue darken-2"
          dark
          fab
        >
          <v-icon v-if="false">
            mdi-close
          </v-icon>
          <v-icon v-else>
            mdi-account-circle
          </v-icon>
        </v-btn>
      </template>
      <v-btn
        fab
        dark
        small
        color="green"
        @click="updateMbr"
      >
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
      <v-btn
        fab
        dark
        small
        color="indigo"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
      <v-btn
        fab
        dark
        small
        color="red"
      >
        <v-icon>mdi-delete</v-icon>
      </v-btn>
    </v-speed-dial> -->
</div>
</template>

<script>
import { Swiper, SwiperSlide, directive } from 'vue-awesome-swiper'
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import {getImgFileExtCommon , ImgfileSizeCheckCommon} from '@/common/nawagoCommonJs'
import {mapGetters , mapActions} from 'vuex'
import store from '@/store/store'
import 'swiper/css/swiper.css'
import videojs from 'video.js';
import 'video.js/dist/video-js.css'

  export default {
    name: 'myPage',
    components : {
    	Swiper,
    	SwiperSlide,
    },
    data: function(){
    	return {
    		isMySelf : false,
    		userId : this.$route.params.userId,
    		profilePhotoYn : false,
    		progress : false,
    		myInfo : {
    			mbrId : null,
    			mbrEmail : null,
    			mbrGrdCd : null,
    			mbrMobileNo : null,
    			mbrNm : null,
    			mbrNickNm : null,
    			mbrTpCd : null,
    			mbrRpstImgUrl : null,
    			mbrRpstImgNm : null,
    			mbrSeq : null
    		},
    		petList : [],
    		myShowOffList : [],
    		authAxios : getAuthCheckedAxios(),
    		profilePhotoUrl : '',
    		loading : false,
   			bottom: false,
   			currentPage : 0,
   			listPageSize : 6,
   			leftShowOffList : false, //더이상 showofflist 남았는지 안남앗는지 여부로 api호출 을 구분
   			swiperOptions: {
     	          pagination: {
     	            el: '.swiper-pagination.s01',
     	          },
     	        },
      		sheet: false,
      		menuSheet : false,
      		replySheet : false,
      		currentShowOffSeq : null,
   			replyCont : null,
   			showOffReplyList : [],
		    btnDisabled : true,
		    //팝업버튼
		    transition: 'slide-y-reverse-transition',
		    fab: false,
		    clickedShowoffSeq : null, //메뉴를 클릭한 스토리번호
    	}
    },
    compoentns : {
    	Swiper,
    	SwiperSlide,
    },
    directives: {
        swiper: directive
    },
	beforeCreate(){
		this.$store.commit('getPublicMbrInfo')
	},
    created : function() {
    	var that = this;
    	that.getMbrInfo();
    },
    mounted : function() {
    	var that = this;
   		window.addEventListener('scroll', function(){
   			that.bottom = that.bottomVisible()
        });
    },
    watch: {
   	    bottom(bottom) {
   	      var that =this;
   	      if (bottom) {
   	    	that.getNextMyShowOffListPage();
   	      }
   	    },
   	 	replySheet: function (newVal, oldVal) {
	 		var that = this;
	 		if(newVal != oldVal){
	 			that.replyCont = null;
	 			that.showOffReplyList = null;
	 		}
	    },
	    replyCont : function(newVal) {
	    	
	    	var that = this;
	    	
	    	if(newVal != null){
	    		newVal = newVal.trim();
	    		if(newVal.length > 0){
	   	    		that.btnDisabled = false;
	   	    	}else{
	   	    		that.btnDisabled = true;
	   	    	}
	    	}else{
	    		that.btnDisabled = true;
	    	}
	    },
	    $route : function(newVal,oldVal) {
	    	if(newVal.fullPath != oldVal.fullPath){
	    		this.$router.go(
	    			this.$router.currentRoute
	    		)
	    	}
	    }
   	 },
	computed : {
		activeFab :function() {
          switch (this.tabs) {
            case 'one': return { class: 'purple', icon: 'account_circle' }
            case 'two': return { class: 'red', icon: 'edit' }
            case 'three': return { class: 'green', icon: 'keyboard_arrow_up' }
            default: return {}
          }
        }, 
   		 
   	 },
	methods : {
		getMbrInfo : function(){
			var that = this;
			
			axios.get('/api/member/getUserInfo',{
				params : {
					'userId' : that.userId
				}
			}).
			then(function(resp){
				if(resp.data == ''){
					alert('존재하지 않는 아이디 입니다.');
					that.$router.push({name : 'showOffList'})
				}else{
					that.myInfo = resp.data.mbrJpa;
					that.petList = resp.data.petJpaList;
					
					if(that.$store.getters.getIsLogin){
						if(that.userId == that.$store.getters.getMbrId ){
							that.isMySelf = true;
						}
					}
					that.getFirstMyShowOffListPage();
				}
			})
			.catch(function(err){
				console.log(err);
			})
			
		},
    	updateMbr : function(){
    		this.$router.push({
    			name : 'updateMember'
    		})
    	},
    	fileClick : function(idx){
   			var $fileEl = document.querySelector('#fileId');
   			$fileEl.click();
   		},
		upload : function() { //파일 업로드 이후
      	  	
   			var that = this;
			var $imgInput = document.querySelector('#fileId');
			
			if(!getImgFileExtCommon($imgInput.files[0])){
				alert('이미지 파일만 등록 가능합니다.');
				return ;
			}
			
			if(!ImgfileSizeCheckCommon($imgInput.files[0])){
		    	alert('이미지는 최대 10MB 까지 등록 가능합니다.');
				return ;
		    }
			
			that.progress = true;// 프로그래스 바
			
		    var fd = new FormData();
		    fd.append('data', $imgInput.files[0]);
		    that.authAxios.post('/api/member/file/upload', fd,  
		    		  {
		    	  		headers: {
		    	    		'Content-Type': 'multipart/form-data'
		      			}
		    		  }
		      )
		    .then( response => {
		    	var imgUrl = response.data;
		    	
		    	that.updateProfilePhoto(imgUrl);
	         })
	          .catch(function (err) {
	        	that.progress = false;// 프로그래스 바
	        	alert('시스템 오류가 발생하였습니다.')
	            console.log('fail upload');
	         });
			
        },
        updateProfilePhoto : function(imgUrl){
        	
        	var that = this;
        	var param = that.setProfilePhotoParam(imgUrl);
        	
        	
        	that.authAxios.post('/api/member/update/profilePhoto',param )
		    .then( response => {
		    	var imgResult = response.data;
		    	that.getMbrInfo();
		    	that.progress = false;// 프로그래스 바
	         })
	          .catch(function (err) {
	        	that.progress = false;// 프로그래스 바
	        	alert('시스템 오류가 발생하였습니다.')
	            console.log('fail update profile');
	         });
        },
        setProfilePhotoParam : function(imgUrl) {
        	var that = this;
        	var param = {
        		'mbrSeq' :	that.myInfo.mbrSeq,
        		'attachFileUrl' : imgUrl
        	}
        	return param;
        },
        delPetImg : async function(){//파일 삭제
	    	
	    	var $imgInput = document.querySelector('#fileId');
	    	if($imgInput.files.length < 1)return;
	    	var that = this;
	    	var param = {
					fileName: that.myInfo.mbrRpstImgNm
			}
			await axios.post('/api/pet/file/delete', param)
		      .then( response => {
	          })
	          .catch(function () {
	            	console.log('FAILURE DELETE!!');
	          });
	    	that.petImgNm = null;
            that.petImgUrl = null;
            document.querySelector('#fileId').value=null;
		},
        timeForToday : function(value) {
        	
   			var that = this;
   			var today = this.$moment.tz('Asia/Seoul');
	        var timeValue = this.$moment(value).tz('Asia/Seoul');
	        var betweenTime = Math.floor((today - timeValue) / 1000 / 60);
	        
	        if (betweenTime < 1) return '방금전';
	        if (betweenTime < 60) {
	            return betweenTime + '분전';
	        }
	        
	        var betweenTimeHour = Math.floor(betweenTime / 60);
	        if (betweenTimeHour < 24) {
	            return betweenTimeHour + '시간전';
	        }
	        
	        var betweenTimeDay = Math.floor(betweenTime / 60 / 24);
	        if (betweenTimeDay < 365) {
	            return betweenTimeDay + '일전';
	        }
	        
	        return parseInt(Math.floor(betweenTimeDay / 365)) + '년전';

   		},
		getFirstMyShowOffListPage: function(){ //처음 인입시 리스트 로드
   			
   			var that = this;
   	   		
   			this.$store.dispatch('PageGetter',{
   				url : '/api/showOff/getMyShowOffList',
   				size : that.listPageSize,
   				page : that.currentPage,
   				param : that.myInfo.mbrSeq
   			})
   			.then(resp =>{
   				if(resp.length > 0 ){
   					that.myShowOffList =  resp;
   					that.leftShowOffList = true;
   				}else{
   					that.leftShowOffList = false;
   				}
   				
   			})
   			.catch(err => {
   				console.log("pageGetter error");
   			})
   			
   		},
   		bottomVisible : function(){
            const scrollY = window.scrollY
            const visible = document.documentElement.clientHeight
            const pageHeight = document.documentElement.scrollHeight - 110
            const bottomOfPage = visible + scrollY >= pageHeight
            return bottomOfPage || pageHeight < visible
         },
		getNextMyShowOffListPage: function($state){//처음 이후 스크롤시  리스트 로드
			
   			var that = this;
   			if(!that.leftShowOffList)return;
			that.loading = true;
   			that.currentPage +=1;
   			this.$store.dispatch('PageGetter',{
   				url : '/api/showOff/getMyShowOffList',
   				size : that.listPageSize,
   				page : that.currentPage,
   				param : that.myInfo.mbrSeq
   			})
   			.then(resp =>{
   				setTimeout(function(){
   					if(resp.length > 0 ){
   						var mergeList = that.myShowOffList.concat(resp);
   						that.myShowOffList = mergeList;
   						that.loading = false;
   						that.leftShowOffList = true;
   					}else{
   						that.loading = false;
   						that.leftShowOffList = false;
   					}
   				},1000);
   			})
   			.catch(err => {
   				console.log("newt pageGetter error");
   			})
   			
   		},
   		checkAuth: function(){
  			 var that = this;
  			store.dispatch('getValidAuth')
	       	.then(function(data){
	       	})
	       	.catch(function(err){
	       		alert('댓글 작성을 위해\n로그인 페이지로 이동합니다.');
	       		that.$router.push({
	       			name:'plainLogin'
	       		})
	       	})
  		},
  		openReply : function(showOffSeq){
  			
  			var that = this;
  			that.getShowOffReplyList(showOffSeq);
  			that.currentShowOffSeq = showOffSeq;
   		
  		},
  		askLogin : function(){
  			if(confirm('로그인을 하셔야 댓글입력이 가능합니다.\n로그인 하시겠습니까?')){
  				this.$router.push({
  					name : '/plainLogin'
  				});
  			}
  		},
  		getShowOffReplyList : function(showOffSeq){
    		var that = this;
    		axios.get('/api/showOff/getShowOffReplyList',{
    			params : {
    				'showOffSeq' : showOffSeq
    			} 
    		})
    		.then(function(resp){
   				that.showOffReplyList = resp.data;
    		})
    		.catch(function(err){
    			console.log(err)
    		});
    		
    	},
		addShowOffReply : function(){
   			var that = this;
   			var showOffReplyJpa = {
   				'mbrSeq'	:	that.myInfo.mbrSeq,
   				'showOffSeq':	that.currentShowOffSeq,
   				'replyCont' :	that.replyCont
   			}
   			var param = {
   				'showOffReplyJpa' : showOffReplyJpa
   			}
   			var $showOffreplyCntSpan = document.querySelector('#showOffreplyCnt' +that.currentShowOffSeq);
   			
   			that.authAxios.post('/api/showOff/addShowOffReply',param)
   			.then(function(resp){
   				var replyCntResult = resp.data;
   				that.getShowOffReplyList(that.currentShowOffSeq);
   				that.replyCont = null;
   				$showOffreplyCntSpan.innerHTML = replyCntResult; 
   			})
   			.catch(function(err){
   				console.log(err);
   			});
   		},
   		getShoOffCont : function(shoOffCont){
			return shoOffCont.replaceAll('\n','</br>');
   		},
   		setPushed : function(myLike){
   			if(myLike == '1'){
   				return 'Y';
   			}else{
   				return 'N';
   			}
   		},
   		updateShowOffLike : function(showOffSeq,myLike,event){
   			
   			var $likeBtn = event.currentTarget;
   			
   			
   			var $likeCnt = event.currentTarget.children[0].lastChild.innerHTML;
			var isPushed = event.currentTarget.getAttribute('data-btn-click');
   			
   			if(isPushed == 'Y'){ //이미 좋아요를 누름
   				if(parseInt($likeCnt) >= 1 ){
   					$likeBtn.style.color='';
   					event.currentTarget.setAttribute('data-btn-click','N');
   				}
   			}else{
   				$likeBtn.style.color='#F48FB1';
   				event.currentTarget.setAttribute('data-btn-click','Y');
   			} 
   			
   			var that = this;
   			var param = {
   				'showOffSeq' : showOffSeq
   			}
   			
   			that.authAxios.post('/api/showOff/updateShowOffLike',param)
   			.then(function(resp){
   				var likeCnt = resp.data;
   				$likeBtn.children[0].lastChild.innerHTML = likeCnt; //좋아요 갯수
   			})
   			.catch(function(err){
   				console.log(err);
   			});
   			
   		},
   		setMenuShowOffSeq : function(showoffSeq){
   			var that = this;
   			that.clickedShowOffSeq = showoffSeq;
   		},
   		deleteShowOff : function(){
   			return;// 준비중
   			var that = this;
   			that.menuSheet = false;
   			var param = {
   	   				'showOffSeq' : that.clickedShowOffSeq
   	   		}
   			if(that.isMySelf){
   				that.authAxios.post('/api/showOff/deleteShowOff',param)
   				.then(function(resp){
   					console.log(resp)
   				})
   				.catch(function(error){
   					alert('실패')
   				});
   			}
   			
   		}
    }
  }
</script>
