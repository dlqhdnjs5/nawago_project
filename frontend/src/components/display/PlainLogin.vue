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
	 				color="#00BFA5"
	 				v-model="email"
	 				label ="이메일"
	 				:rules="emailRule"
	 			>
	 			</v-text-field>
	 			<v-text-field
	 				color="#00BFA5"
	 				v-model="password"
	 				type="password"
	 				label ="패스워드"
	 				:rules="passwordRule"
	 			>
	 			</v-text-field>
	 			
	 			<v-btn block large depressed dark color="#00BFA5"
	 				@click="loginProccese">
	 				로그인
	 			</v-btn>
	 			</v-form>
	 			<br>
	 			<v-btn block large depressed dark color="#F48FB1"
	 			@click="$router.push({
	 			name : 'RegisterPage'
	 			})"
	 			>
	 			회원가입
	 			</v-btn>
	 		</v-flex>
	 	</v-layout>
	 </v-container>
</template>

<script>
import {mapActions} from 'vuex'
import router from '@/router/index.js'

export default {
  name: 'PlainLogin',
  data : function() {
    return {
    	valid: true,
    	email : null,
    	password : null,
    	emailRule: [
	        value => !!value || '이메일을 입력해 주세요.',
	      ],
	    passwordRule: [
		    value => !!value || '패스워드를 입력해주세요.',
		  ],
    }
  },
  created: function( ){
	 
  },
  methods: {
	...mapActions(['login']),
	loginProccese : function() {
		if(!this.$refs.form.validate()) return;
		var that = this;
    	var params = {
    			email : that.email , 
    			password : that.password
    	}
    	that.login(params);
    	
	},
  }
 
}
</script>
