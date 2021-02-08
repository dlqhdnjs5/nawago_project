<template>
	<v-container style="max-width:100%">
		<v-layout>
			<span class="font-weight-bold" style="color:#00BFA5;">{{petInfo.petNm}}</span>
			<span> 의 프로필</span>
			<v-icon>mdi-paw</v-icon>
		</v-layout>
		
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
	 				v-model="petNmModel"
	 				:rules = "petNmRules"
	 				label ="*이름">
	 			</v-text-field>
	 			<v-textarea
		          outline
		          v-model="petIntroModel"
		          :rules="petIntroRules"
		          label="*소개글"
		        ></v-textarea>
		        
	 			<div class="text-center" >
	 			
					<v-btn
				      class="mx-2"
				      fab
				      dark
				      large
				      color="#00BFA5"
				      :disabled="!valid"
				      @click="updatePet"
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
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import store from '@/store/store'

  export default {
    name: 'updatePet',
	data : function() {
		return {
			valid: true,
			snackbar: false,
			petSeq : this.$route.query.petSeq,
			authAxios : getAuthCheckedAxios(),
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
			petNmModel : '',
			petIntroModel : '',
		    text: '',
			mbrId : '',
			password : '',
			passwordConfirm : '',
			name : '',
			bfMyInfo : {},
			myInfo : {},
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
		}
	},
	beforeCreate : function(){
		//this.$store.commit('getPublicMbrInfo')
		
   	},
	created : function(){
		var that = this;
		that.getMbrInfo();
	},
	methods : {
		getPetInfo :  function(){
			var that = this;
			
			axios.get('/api/pet/getPetInfo',{
				params : {
					'petSeq' : that.petSeq 
				}
			})
			.then(function(resp){
				that.petInfo = resp.data;
				console.log(that.petInfo);
				var mbrSeq = that.myInfo.mbrSeq;
				
				/*
				 url 침투 방어
				*/
				if(mbrSeq != that.petInfo.mbrPetMappingJpa.mbrSeq){
					alert('같은 식구원만 식구의 프로필 변경이 가능합니다.');
					 that.$router.push({
						 name : 'showOffList'
					 })
				}
				
				that.petNmModel = that.petInfo.petNm;
				that.petIntroModel = that.petInfo.petIntro;
				
				
			})
			.catch(function(err){
				console.log(err);
			})
		},
		getMbrInfo : function(){
    		var that = this;
    		store.dispatch('getMbrInfo')
        	.then(function(data){
        		that.myInfo = data;
        		that.getPetInfo();
        	})
        	.catch(function(err){
        		console.log(err)
        	})
    	},
		getIntroCheck : function(value){
			if(value.includes('>') || value.includes('<')){
				return false;
			}else{
				return true;
			}
		},
		checkEx : function(value){
			var that = this;
			var pattern_spc = /[~!@#$%^&*()_+|<>?:{}]/; // 특수문자
			
			return !that.checkBlank(value) && !pattern_spc.test(value);
		},
    	updatePet : function() {
			
			var that = this;
			if(this.$refs.form.validate()){
	    	   
				var param = {
						petSeq		: that.petSeq,
	    	   			petNm		: that.petNmModel,
	    	   			petIntro	: that.petIntroModel,
	    	   	}
				 
				that.authAxios.post('/api/pet/update',param)
 	   		   .then(function(resp){
 	   			alert('프로필 변경이 완료되었습니다.')
 	   			that.$router.push({
 	   				path: '/pet/'+that.myInfo.mbrSeq+'/'+that.petSeq
 	   			})
 	   		   })
 	   		   .catch(function(err){
 	   			   alert('시스템 오류가 발생하였습니다.');
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
	},
	mounted : function(){
		var that = this;
		
		
		
	}
  }
</script>
