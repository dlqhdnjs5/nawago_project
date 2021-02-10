<template>
	<v-container style="max-width:100%">
	 	<v-layout align-center>
	 		<v-flex xs12 >
	 		<v-form
			    ref="form"
			    v-model="valid"
			    lazy-validation
			  >
	 			<v-text-field
	 				v-model="mbrId"
	 				color="#00BFA5"
	 				id="mbrId"
	 				:rules="mbrIdRules"
	 				label ="*아이디">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="password"
	 				color="#00BFA5"
	 				type="password"
	 				:rules="passwordRules"
	 				label ="*패스워드">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="passwordConfirm"
	 				color="#00BFA5"
	 				type="password"
	 				:rules="passwordCfRules"
	 				label ="*패스워드 확인">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="nickname"
	 				color="#00BFA5"
	 				:rules = "nicknameRules"
	 				label ="*닉네임">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="mbrEmail"
	 				color="#00BFA5"
	 				:rules ="mbrEmailRules"
	 				label ="*이메일">
	 			</v-text-field>
	 			<v-text-field
	 				v-model="name"
	 				color="#00BFA5"
	 				:rules ="nameRules"
	 				label ="이름">
	 			</v-text-field>
	 			<v-btn block  depressed color="#00BFA5" dark
	 			:disabled="!valid"
	 			@click="joinMbr"
	 				>
	 				가입하기
	 			</v-btn>
	 		
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
import router from '@/router/index.js'
  export default {
    name: 'RegisterPage',
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
			mbrIdRules: [
		        value => !!value || '아이디를 입력해주세요.',
		        value => (value && value.length <= 15) || '최대 15자까지 입력 가능합니다.',
		        value => (value && value.length >= 5 && this.checkMbrIdVd(value)) || '5자이상의 영문 혹은 영문+숫자를 입력해주세요.',
		      ],
		   passwordRules: [
		        value => !!value || '비밀번호를 입력해주세요.',
		        value => (!this.checkBlank(value)) || '공백을 제외한 비밀번호를 입력해주세요.',
		        value => (value && value.length >= 8) || '최소 8자이상의 암호를 입력해주세요.',
		      ],
		   passwordCfRules: [
			     value => (!!value && this.checkPwConfirm(value)) || '비밀번호를 확인해주세요.',
			  ],
		   nicknameRules: [
		        value => !!value || '닉네임을 입력해주세요.',
		        value => (value && value.length  <= 15) || '닉네임은 최대 15자까지 입력 가능합니다.',
		        value => (value && value.length  >= 2) || '닉네임을 최소 2자이상 입력해주세요.',
		        value => (this.checkEx(value)) || '특수문자나 공백을 제외하고 입력해 주세요.'
		      ],
		   mbrEmailRules: [
		        value => !!value || '이메일을 입력해주세요.',
		        value => (this.checkEmail(value)) || '옳바른 이메일 형식을 입력해 주세요.',
		        value => (value && value.length <= 20) || '이메일은 최대 20자까지 입력 가능합니다.',
		      ],
		   nameRules: [
			   value => (value.length  <= 15) || '이름은 최대 15자까지 입력 가능합니다.',
			   value => (!this.checkBlank(value)) || '공백을 제외한 이름을 입력해주세요.',
		      ],      
		}
	},
	methods : {
		joinMbr () {
			
			var that = this;
			if(this.$refs.form.validate()){
	    	   
	    	   that.isExistMbrId()
	    	   .then(function(resp){
	    	   	   if(resp == 'SUCCESS'){
	    	   		   
	    	   		   var param = {
	    	   				mbrPw : that.password
	    	   		   };
	    	   		   var mbrJpa = {
	    	   				mbrId		:that.mbrId,
	    	   				mbrPw 		: that.password,
	    	   				mbrNickNm	: that.nickname,
	    	   				mbrEmail	: that.mbrEmail,
	    	   				mbrNm		: that.name
	    	   		   }
	    	   			param['mbrJpa'] = mbrJpa;
	    	   		   axios.post('/api/join',param)
	    	   		   .then(function(resp){
	    	   			alert('회원가입이 완료되었습니다.\n 로그인 페이지로 이동합니다.')
	    	   			router.push({
	    	   				name : 'plainLogin'
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
	    		   }else if(resp=='EMAIL'){
	    			   that.text = '이메일이 중복되었습니다.'
	    			   that.mbrEmail='';
	    			   that.snackbar = true;
	    		   }else if(resp=='NICKNM'){
	    			   that.text = '닉네임이 중복되었습니다.'
	    			   that.nickname='';
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
					mbrId 		: that.mbrId,
					mbrNickNm	: that.nickname,
					mbrEmail 	: that.mbrEmail
			}
			return new Promise(function(resolve, reject) {
				axios.post('/api/member/isExistMbrInfo',param)
				.then(function(data){
					var result = data.data;
					resolve(result)
				})
			}); 
		}
	},
	created : function(){
		
	},
	mounted : function(){
	}
  }
</script>
