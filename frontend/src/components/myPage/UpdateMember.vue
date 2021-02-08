<template>
	<v-container style="max-width:100%">
		<span >프로필 변경</span>
		<v-divider style="padding-top : 40px;">
		</v-divider>
	 	<v-layout align-center>
	 		<v-flex xs12 >
	 		<v-form
			    ref="form"
			    v-model="valid"
			    lazy-validation
			  >
	 			<v-text-field
	 				v-model="nickname"
	 				:rules = "nicknameRules"
	 				label ="*닉네임">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="mbrEmail"
	 				:rules ="mbrEmailRules"
	 				label ="*이메일">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="name"
	 				:rules ="nameRules"
	 				label ="이름">
	 			</v-text-field>
	 			<div class="text-center" >
	 			
					<v-btn
				      class="mx-2"
				      fab
				      dark
				      large
				      color="#00BFA5"
				      :disabled="!valid"
				      @click="updateMbr"
				    >
				      <v-icon dark>
				        mdi-check-bold
				      </v-icon>
				    </v-btn>
				
	 			</div>
	 			
				
				
	 		
	 			</v-form>
			    
			    <v-snackbar
			      v-model="snackbar"
			    >
			      {{ text }}
			
			      <template v-slot:action="{ attrs }">
			        <v-btn
			          color="pink"
			          text
			          v-bind="attrs"
			          @click="snackbar = false"
			        >
			          Close
			        </v-btn>
			      </template>
			    </v-snackbar>
	 		</v-flex>
	 	</v-layout>
	 </v-container>
</template>

<script>
import {mapGetters , mapActions} from 'vuex'
import router from '@/router/index.js'
import store from '@/store/store'
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'


  export default {
    name: 'updateMember',
	data : function() {
		return {
			valid: true,
			snackbar: false,
		    text: '',
			mbrId : '',
			password : '',
			passwordConfirm : '',
			nickname : '',
			mbrEmail : '',
			name : '',
			bfMyInfo : {},
			authAxios : getAuthCheckedAxios(),
			myInfo : {},
			/* mbrIdRules: [
		        value => !!value || '아이디를 입력해주세요.',
		        value => (value && value.length <= 15) || '최대 15자까지 입력 가능합니다.',
		        value => (value && value.length >= 5 && this.checkMbrIdVd(value)) || '5자이상의 영문 혹은 영문+숫자를 입력해주세요.',
		      ], */
		   nicknameRules: [
		        value => !!value || '닉네임을 입력해주세요.',
		        value => (value && value.length  <= 13) || '닉네임은 최대 13자까지 입력 가능합니다.',
		        value => (value && value.length  >= 2) || '닉네임을 최소 2자이상 입력해주세요.',
		        value => (this.checkEx(value)) || '특수문자나 공백을 제외하고 입력해 주세요.'
		      ],
		   mbrEmailRules: [
		        value => !!value || '이메일을 입력해주세요.',
		        value => (this.checkEmail(value)) || '옳바른 이메일 형식을 입력해 주세요.',
		        value => (value && value.length <= 20) || '이메일은 최대 20자까지 입력 가능합니다.',
		      ],
		   nameRules: [
			   value => !!value || '이름을 입력해주세요.',
			   value => (value.length  <= 8) || '이름은 최대 8자까지 입력 가능합니다.',
			   value => (!this.checkBlank(value)) || '공백을 제외한 이름을 입력해주세요.',
		      ],      
		}
	},
	beforeCreate : function(){
   	},
   	computed: {
    },
	methods : {
		getMbrInfo : function(){
    		var that = this;
        	store.dispatch('getMbrInfo')
        	.then(function(data){
        		that.myInfo = data;
        		console.log(data)
        		
        		that.nickname = that.myInfo.mbrNickNm
        		that.mbrEmail = that.myInfo.mbrEmail
        		that.name = that.myInfo.mbrNm
        		
        	})
        	.catch(function(err){
        		console.log(err)
        	})
    	},
    	updateMbr : function() {
			
			var that = this;
			if(this.$refs.form.validate()){
	    	   
	    	   that.isExistMbrId()
	    	   .then(function(resp){
	    	   	   if(resp == 'SUCCESS'){
	    	   		   
	    	   		   var param = {
	    	   				mbrNickNm	: that.nickname,
	    	   				mbrEmail	: that.mbrEmail,
	    	   				mbrNm		: that.name,
	    	   				mbrSeq 		: that.myInfo.mbrSeq
	    	   		   }
	    	   			that.authAxios.post('/api/member/update',param)
	    	   		   .then(function(resp){
	    	   			alert('프로필 변경이 완료되었습니다.')
	    	   			router.push({
	    	   				name : 'myPage'
	    	   			})
	    	   		   })
	    	   		   .catch(function(err){
	    	   			   alert('시스템 오류가 발생하였습니다.');
	    	   		   })
	    	   	   }
	    		   else if(resp=='ID'){
	    			   that.text = '아이디가 중복되었습니다.'
	    			   that.mbrId='';
	    			   that.snackbar = true;
	    		   }else if(resp=='NICKNM'){
	    			   that.text = '닉네임이 중복되었습니다.'
	    			   that.nickname='';
	    			   that.snackbar = true;
	    			   
	    		   }else if(resp=='EMAIL'){
	    			   that.text = '이메일이 중복되었습니다.'
		    			   that.mbrEmail='';
		    			   that.snackbar = true;
		    		}
	    	    })
	    	   .catch(function(err){
	       			console.log(err)
	       		})
	    	   
	    	   
	    	   
	       }
	    },
		checkMbrIdVd : function(id){
			var regType1 = /^[A-Za-z0-9+]*$/
			return regType1.test(id);
		},
		checkPwConfirm : function(pw){
			var that = this;
			return pw == that.password;
		},
		checkBlank : function(value){
			var blank_pattern = /[\s]/g;
			return blank_pattern.test(value);
		},
		checkEmail : function(email){
			var reg_email = /^([0-9a-zA-Z_\.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
			return reg_email.test(email);
		},
		checkEx : function(value){
			var that = this;
			var pattern_spc = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자
			
			return !that.checkBlank(value) && !pattern_spc.test(value);
		},
		isExistMbrId :  function(){
			var that = this;
			var param = {
					mbrSeq		: that.myInfo.mbrSeq,
					mbrNickNm	: that.nickname,
					mbrEmail 	: that.mbrEmail,
	   				bfMbrEmail  : that.myInfo.mbrEmail,
	   				bfMbrNickNm : that.myInfo.mbrNickNm
			}
			return new Promise(function(resolve, reject) {
				axios.post('/api/member/isExistMbrInfoForUpdate',param)
				.then(function(data){
					var result = data.data;
					resolve(result)
				})
			}); 
		}
	},
	created : function(){
		var that = this;
		that.getMbrInfo();
	},
	mounted : function(){
		var that = this;
		
		
		
	}
  }
</script>
