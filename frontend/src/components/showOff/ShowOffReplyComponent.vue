<template>
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
						      <img v-if="mbrInfo != null"
					      		:src="mbrInfo.mbrRpstImgUrl +'/'+  mbrInfo.mbrRpstImgNm "
					      	  >
						    </v-avatar>
							<v-text-field
								label="댓글달기"
								outlined
								dense
								style="padding-left:2%;"
								@focus="checkAuth"
								v-model="replyCont"
							>
							</v-text-field>
						
							<v-btn
								id="addReplyBtn"
								style="padding-left:9%;"
								icon
								color="blue"
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
					      		src="@/assets/anonymous.png"
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
	
</template>

<script>
import InfiniteLoading from 'vue-infinite-loading'
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import store from '@/store/store'
import {mapGetters , mapActions} from 'vuex'

  export default {
    name: 'ShowOffReplyComponent',
    components : {
    },
    props : ['mbrInifo','showOffSeq'],
   	data : function(){
   		return {
   			authAxios : getAuthCheckedAxios(),
   			//sheet: false,
   			currentShowOffSeq : null,
   			replyCont : null,
   			showOffReplyList : [],
		    btnDisabled : true,
		    sheet : false,
   			/*myInfo : {
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
    		}, */
   		}
   	},
   	computed: {
        ...mapGetters({
   			mbrInfo : 'getMbrInfo',
   			isLogin : 'getIsLogin'
   		}),
    },
   	created : function(){
//    		this.$store.commit('getPublicMbrInfo')
   		var that = this;
   		that.openReply(that.showOffSeq)
		
   	},
   	mounted : function() {
   	},
   	watch: {
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
   			that.sheet = !that.sheet;
   			that.getShowOffReplyList(showOffSeq);
   			that.currentShowOffSeq = showOffSeq;
   			//that.getMbrInfo();
    		
   		},
   		/* getMbrInfo : function(){
    		var that = this;
        	store.dispatch('getMbrInfo')
        	.then(function(data){
        		that.myInfo = data;
        		console.log(data);
        	})
        	.catch(function(err){
        		console.log(err)
        	})
    	}, */
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
	   		var today = new Date();
	        var timeValue = new Date(value);
	        
	        var betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
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
	        
	        return String(Math.floor(betweenTimeDay / 365)) + '년전';

   		}
   		
   	}
  }
</script>
