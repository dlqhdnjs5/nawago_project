<template>
	<v-container style="max-width:100%">
		<v-layout row wrap  xs12>
			
			로그인
		
			<v-flex xs12>
			
			<v-form
			    ref="form"
			    v-model="valid"
			    lazy-validation
			  >
			    <v-text-field
			      v-model="name"
			      :counter="10"
			      :rules="nameRules"
			      label="아이디"
			      id="email"
			      required
			    ></v-text-field>
			
			   <v-text-field 
			    label="암호"
			     v-model="password"
			    id="password"
			    type="password"
			    >
			    </v-text-field>
			    
			    
			    <v-btn
				  block
				  elevation="9"
				  @click="loginProccese"
				>
				로그인
				</v-btn>
			  </v-form>
			</v-flex>
		</v-layout>
	</v-container>
</template>

<script>
import axios from 'axios'

export default {
  name: 'LoginPage',
  data : function() {
    return {
    	valid: true,
        name: '',
        password : '',
        nameRules: [
          value => !!value || '아이디를 입력해 주세요',
          value => (value && value.length <= 20) || 'Name must be less than 10 characters',
        ],
        email: '',
        emailRules: [
          v => !!v || 'E-mail is required',
          v => /.+@.+\..+/.test(v) || 'E-mail must be valid',
        ],
        select: null,
        checkbox: false,
        storeTestInput : '',
    }
  },
  created: function( ){
	 
  },
  methods: {
      validate () {
        this.$refs.form.validate()
      },
      reset () {
        this.$refs.form.reset()
      },
      resetValidation () {
        this.$refs.form.resetValidation()
      },
      loginProccese : function() {
    	
    	var that = this;
    	alert(that.email)
    	alert(that.password)
    	var params = {
    			email : that.email , 
    			password : that.password
    	}
    	
    	axios.post('/loginProcesse', params )
    	.then(function(resp){
    		localStorage.setItem('x-auth', resp.data);
    		that.roturtest();
    	})
    	.catch(function(error){
    		alert(error.response.data);
    	});
    	
      },
      roturtest : function(){
    	  var router = this.$router;
    	  router.push('/test');
      },
      checkAuth : function(){
    	  
    	  axios.get('/authenticated')
    	  .then(function(resp){
        		console.log(resp);
        	})
          .catch(function(error){
        	
          });
    	  
      },
 	  testApi : function(){
 		 
    	  var config = {
    			  headers : { 'x-auth' : localStorage.getItem('x-auth') }
    	  }
    	  
    	  axios.get('/auth/member/api/checkMbrId?mbrId=brandon',config)
    	  .then(resp => {
        		console.log(resp);
        	})
          .catch(function(error){
        	 console.log(error) 
          });
    	  
      }
  }
 
}
</script>
