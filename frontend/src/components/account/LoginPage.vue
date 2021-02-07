<template>
	<v-container style="margin-top : 20%;" class="text-center" >
	
		<v-layout row wrap >
		 		<v-flex xs12 md12  >
		 			<v-text-field
		 				v-model="email"
		 				label ="이메일">
		 			</v-text-field>
		 			<v-text-field
		 				v-model="password"
		 				type="password"
		 				label ="패스워드">
		 			</v-text-field>
		 			<v-btn block large depressed color="primary"
		 				@click="loginProccese">
		 				로그인
		 			</v-btn>
		 		</v-flex>
		</v-layout>
	</v-container>
</template>

<script>
import {mapActions} from 'vuex'

export default {
  name: 'LoginPage',
  data : function() {
    return {
    	email : null,
    	password : null
    }
  },
  created: function( ){
	 
  },
  methods: {
	...mapActions(['login']),
	loginProccese : function() {
		
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
