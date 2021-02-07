<template>
	
	<v-layout row wrap pt-5 text-xs-center>
		<div>
			예제 
		</div>
	
		<v-flex xs12>
		
			
			<v-btn 
			@click="$router.push(
			{name: 'HelloWorld2',
			 params : {
			 	id  : '시발'
			 }
			})"
			>
			params 노출x
			</v-btn>
			<v-btn 
			@click="$router.push(
			{name: 'HelloWorld2',
			 query : {
			 	group  : '시발'
			 }
			})"
			>
			 쿼리
			</v-btn>
			스토어 테스트 입력값테스트1	 : {{$store.getters.getTestState}}
			스토어 테스트  입력값테스트2 : {{getStateFromMapGetters}}
			 <v-text-field
			  v-model="storeTestInput">
			 </v-text-field>
			<v-btn
			@click="$store.dispatch('setTestStateMut',storeTestInput)">
				스토어 입력
			</v-btn>
			<v-btn
			@click="setTestStateMut(storeTestInput)">
				스토어 입력2
			</v-btn>
		
		</v-flex>
	</v-layout>
</template>
<script>
import axios from 'axios'
import {mapGetters , mapActions} from 'vuex'

export default {
  name: 'Example',
  data : function() {
    return {
        storeTestInput : '',
    }
  },
  computed :{
	...mapGetters({
		getStateFromMapGetters : 'getTestState'
	})
  },
  created: function( ){
	 
  },
  methods: {
      roturtest : function(){
    	  var router = this.$router;
    	  router.push('/test');
      },
      ...mapActions(['setTestStateMut'])
  }
 
}
</script>
