<template>

<div class="showOffList">
	<div class="text-center">
			<v-layout v-if="topLoading" xs12 sm12 md12 lg12 xl12 >
				<v-flex>
					<v-progress-circular
				      indeterminate
				      color="#00BFA5"
				    ></v-progress-circular>
				</v-flex>
			</v-layout>
	</div>
	<div>
		<v-layout row wrap>	
			<v-flex xs12 sm6 md4 lg4 xl4
					v-if="showOffList != null "
					v-for="showOffObj in  showOffList" >
			<v-card
			    max-width="344"
			    class="mx-auto"
			  >
			    <v-list-item>
			      <v-list-item-avatar color="grey">
			      	<img
			      		v-if="showOffObj.showOffJpa.mbrJpa.mbrRpstImgUrl != null &&  showOffObj.showOffJpa.mbrJpa.mbrRpstImgNm != null"
			      		:src="showOffObj.showOffJpa.mbrJpa.mbrRpstImgUrl + '/' + showOffObj.showOffJpa.mbrJpa.mbrRpstImgNm"
			      		@click="$router.push({path: '/user/'+showOffObj.showOffJpa.mbrJpa.mbrId})"
			      	>
			      	<img
			      		v-else
			      		src="@/assets/emptyProfile2.png"
			      		@click="$router.push({path: '/user/'+showOffObj.showOffJpa.mbrJpa.mbrId})"
			      	>
			      </v-list-item-avatar>
			      <v-list-item-content>
			        <v-list-item-title v-text="showOffObj.showOffJpa.mbrJpa.mbrNickNm"></v-list-item-title>
			        <v-list-item-subtitle v-text="timeForToday(showOffObj.showOffJpa.regDt)"></v-list-item-subtitle> 
			      </v-list-item-content>
			    </v-list-item>
			    <template v-if="showOffObj.showOffJpa.showOffAttachJpa.length > 1">
			    	<swiper ref="mySwiper" 
						class="swiper"
						:options="swiperOptions" 
						v-if="showOffObj.showOffJpa.showOffAttachJpa != null" 
						@slideChange="slideChanged(swiper)"
					> 
					    <swiper-slide v-for="(showOffAttachObj , conttIdx ) in showOffObj.showOffJpa.showOffAttachJpa" >
					    	<template v-if="showOffAttachObj.showOffAttachTpCd == 'IMG'">
						    	<v-img 
							      :src="showOffAttachObj.showOffAttachUrl + '/' + showOffAttachObj.showOffAttachNm"
							      height="295"
							    ></v-img>
					    	</template>
					    	 <template v-else>
						    	<video
							      	:id="'clientMov'+conttIdx"
							      	class="video-js vjs-default-skin vjs-big-play-centerd" 
							      	preload="metadata" 
							      	playsinline
							      	style="margin:0 auto;height:295px;width:344px;"
							      	controls
							      	:src="showOffAttachObj.showOffAttachUrl + '/' + showOffAttachObj.showOffAttachNm +'#t=0.5'"
						      	>
						      	</video>
						      	<!-- autoplay  -->
						      	<!-- muted="muted" -->
						      	<!-- loop  -->
					    	</template> 
					    </swiper-slide> <div class="swiper-pagination s01" slot="pagination"></div>
				    </swiper>
			    </template>
			    <template v-else-if="showOffObj.showOffJpa.showOffAttachJpa.length == 1">
			    	<template v-if="showOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachTpCd == 'IMG'">
			    		<v-img 
					      :src="showOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachUrl + '/' + showOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachNm"
					      height="295"
				   	 ></v-img>
			    	</template>
			    	<template v-else>
	    				<video
					      	class="video-js vjs-default-skin vjs-big-play-centerd" 
					      	playsinline
					      	preload="metadata" 
					      	style="margin:0 auto;height:295px;width:344px;"
					      	controls
					      	:src="showOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachUrl + '/' + showOffObj.showOffJpa.showOffAttachJpa[0].showOffAttachNm +'#t=0.5'"
				      	>
				      	</video>
			    	</template>
			    	
			    </template>
			    <v-card-text v-html="getShoOffCont(showOffObj.showOffJpa.showOffCont)">
			    </v-card-text>
			
			    <v-card-actions>
			      <v-btn
			         color="#F48FB1"
			        icon
			      >
			        <v-icon>mdi-heart-outline</v-icon>	<!-- mdi-bone -->
			      </v-btn>
			      <v-btn
			       color="#00BFA5"
			        icon
			        @click="sheet = !sheet; openReply(showOffObj.showOffJpa.showOffSeq);"
			      >
			       <v-icon>mdi-chat-outline</v-icon>	
			       <span>{{showOffObj.replyCnt}}</span>
			      </v-btn>
			      <v-spacer></v-spacer>
			    </v-card-actions>
			  </v-card>
			</v-flex>
		</v-layout>
		<div class="text-center">
			<v-layout v-if="loading" xs12 sm12 md12 lg12 xl12 >
				<v-flex>
					<v-progress-circular
				      indeterminate
				      color="#00BFA5"
				    ></v-progress-circular>
				</v-flex>
			</v-layout>
		</div>
	</div>
	
	<div class="text-center">
		<v-bottom-sheet
		v-model="sheet"
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
					@click="sheet = !sheet"
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
							<template v-if="isLogin && mbrInfo != null">
								<img v-if="mbrInfo.mbrRpstImgUrl != null && mbrInfo.mbrRpstImgNm != null"
					      		:src="mbrInfo.mbrRpstImgUrl +'/'+  mbrInfo.mbrRpstImgNm "
						      	>
						      	<img v-else
						      		src="@/assets/emptyProfile2.png"
						      	>
							</template>
						    </v-avatar>
							<v-text-field v-if="isLogin"
								label="댓글달기"
								outlined
								dense
								style="padding-left:2%;"
								color="#00BFA5"
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
	
</div>
	
</template>

<script>
import { Swiper, SwiperSlide, directive } from 'vue-awesome-swiper'
import InfiniteLoading from 'vue-infinite-loading'
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import store from '@/store/store'
import {mapGetters , mapActions} from 'vuex'
import 'swiper/css/swiper.css'
import videojs from 'video.js';
import 'video.js/dist/video-js.css'

  export default {
    name: 'ShowOffList',
    components : {
    	Swiper,
    	SwiperSlide,
    	InfiniteLoading,
    	/* ShowOffReplyComponent */
    },
    directives: {
    },
   	data : function(){
   		return {
   			valid : true,
   			authAxios : getAuthCheckedAxios(),
   			sheet: false,
   			swiperOptions: {
   	          pagination: {
   	            el: '.swiper-pagination.s01',
   	          },
   	          // Some Swiper option/callback...
   	        },
   			show : false,
   			showOffList : [],
   			currentPage : 0,
   			listPageSize : 6,
   			loading : false,
   			topLoading : false,
   			bottom: false,
   			currentShowOffSeq : null,
   			replyCont : null,
   			showOffReplyList : [],
		    btnDisabled : true,
   		}
   	},
   	computed: {
        swiper() {
          return this.$refs.mySwiper.swiper
        },
        ...mapGetters({
   			mbrInfo : 'getMbrInfo',
   			isLogin : 'getIsLogin'
   		}),
    },
    beforeCreate(){
		this.$store.commit('getPublicMbrInfo')
	},
   	created : function(){
   		var that = this;
		that.getFirstShowOffListPage();
		
   	},
   	mounted : function() {
   		var that = this;
   		
   		window.addEventListener('scroll', function(){
   			that.bottom = that.bottomVisible()
   			that.topVisible();
        });
   		
   		setTimeout(function(){
   			var mySwiper = that.$refs.mySwiper;
   		},1000);
   		
   	},
   	watch: {
   	    bottom(bottom) {
   	      var that =this;
   	      if (bottom) {
   	    	that.getNextShowOffListPage();
   	      }
   	    },
   	 	sheet: function (newVal, oldVal) {
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
   	    }
   	    
   	 },
   	methods : {
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
   			//that.getMbrInfo();
    		
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
   	 	bottomVisible : function(){
         const scrollY = window.scrollY
         const visible = document.documentElement.clientHeight
         const pageHeight = document.documentElement.scrollHeight - 110
         const bottomOfPage = visible + scrollY >= pageHeight
         return bottomOfPage || pageHeight < visible
		},
		topVisible : function(){
			
			var that = this;
	         const scrollY = window.scrollY
	         const visible = document.documentElement.clientHeight
	         const pageHeight = document.documentElement.scrollHeight
	         const bottomOfPage = visible + scrollY <= pageHeight
	         
	         if(scrollY < -200 && visible < 800){
	        	 that.topLoading = true;
	        	 that.getFirstShowOffListPage();
	         }
	         
		},
		getFirstShowOffListPage: function(){ //처음 인입시 리스트 로드
  			var that = this;
  			that.showOffList = [];
  			this.$store.dispatch('PageGetter',{
  				url : '/api/showOff/list',
  				size : that.listPageSize,
  				page : that.currentPage
  			})
  			.then(resp =>{
  				that.showOffList =  resp;
  				that.topLoading = false;
  			})
  			.catch(err => {
  				console.log("pageGetter error");
  			})
  			
		},
		getNextShowOffListPage: function($state){//처음 이후 스크롤시  리스트 로드
			
   			var that = this;
			that.loading = true;
   			that.currentPage +=1;
   			this.$store.dispatch('PageGetter',{
   				url : '/api/showOff/list',
   				size : that.listPageSize,
   				page : that.currentPage
   			})
   			.then(resp =>{
   				setTimeout(function(){
   					if(resp.length > 0 ){
   						var mergeList = that.showOffList.concat(resp);
   						that.showOffList = mergeList;
   						that.loading = false;
   					}else{
   						that.loading = false;
   					}
   				},1000);
   			})
   			.catch(err => {
   				console.log("pageGetter error");
   			})
   			
		},
   		addShowOffReply : function(){
   			
   			//if(!this.$refs.form.validate())return false;
   			
   			var that = this;
   			var showOffReplyJpa = {
   				'mbrSeq'	:	that.mbrInfo.mbrSeq,
   				'showOffSeq':	that.currentShowOffSeq,
   				'replyCont' :	that.replyCont
   			}
   			var param = {
   				'showOffReplyJpa' : showOffReplyJpa
   			}
   			that.authAxios.post('/api/showOff/addShowOffReply',param)
   			.then(function(resp){
   				that.getShowOffReplyList(that.currentShowOffSeq);
   				that.replyCont = null;
   			})
   			.catch(function(err){
   				console.log(err);
   			});
   		},
   		timeForToday : function(value) {
   			
   			var that = this;
	   		var today = this.$moment.tz('Asia/Seoul');
	        var timeValue = this.$moment(value).tz('Asia/Seoul');
	        var betweenTime = Math.floor((today - timeValue) / 1000 / 60);
	       
	        if (betweenTime < 1) return '방금전';
	        if (betweenTime < 60) {
	            return parseInt(betweenTime) + '분전';
	        }
	        
	        var betweenTimeHour = Math.floor(betweenTime / 60);
	        if (betweenTimeHour < 24) {
	            return parseInt(betweenTimeHour) + '시간전';
	        }
	        
	        var betweenTimeDay = Math.floor(betweenTime / 60 / 24);
	        if (betweenTimeDay < 365) {
	            return parseInt(betweenTimeDay) + '일전';
	        }
	        
	        return Math.floor(betweenTimeDay / 365) + '년전'; 
	        

   		},
   		renderDate : function(date){
   		   var d = new Date(date)
           var month = d.getMonth() + 1
           var day = d.getDate()
           var year = d.getFullYear()
           var hour = d.getHours()
           var minutes = d.getMinutes()
           var seconds = d.getSeconds()
           return year+'-'+month + '-' + day  +   ' ' + hour + ':' + minutes + ':' + seconds;    
   		},
   		getShoOffCont : function(shoOffCont){
			return shoOffCont.replaceAll('\n','</br>');
   		},
   		slideChanged : function(event){
   		}
   		
   	}
  }
</script>
