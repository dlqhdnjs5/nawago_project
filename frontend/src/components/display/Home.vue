<template>
  <div class="home">
    <img alt="Vue logo" src="../../assets/logo.png">
    <router-view>
	      
	</router-view>
	<button @click="gogo">
		버튼
	</button>
	<router-link to="/login">Link To Login</router-link>
	<router-link to="/plain/plainLogin">plainLogin</router-link>
	<button @click="$router.push({name : 'plainLogin'})">
		플레인로그인
	</button>
	<v-btn
	@click="$store.dispatch('logout')">
	로그아웃
	</v-btn>
  </div>
</template>
<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
	
  name: 'Home',
  components: {
    HelloWorld
  },
  methods : {
	  gogo : function(){
		  var router = this.$router;
		  router.push('/login');
	  }
  }
  
}
</script>
