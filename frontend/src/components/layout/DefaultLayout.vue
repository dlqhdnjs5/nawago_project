<template>
	<div class="overflow-hidden" >
		<v-app-bar fixed dense app>
		  <v-toolbar-title>NawaGo</v-toolbar-title>
		  <v-spacer></v-spacer>
		  <v-toolbar-items class="hidden-sm-and-down">
		  </v-toolbar-items>
		  <v-spacer></v-spacer>
		<v-app-bar-nav-icon @click="drawer = !drawer"></v-app-bar-nav-icon>
		</v-app-bar>
		
		<v-main >
			 
		    <!-- Provides the application the proper gutter -->
			<v-sheet
      		  class="overflow-y-auto"
      		   
		    >	
				<v-container  grid-list-md text-xs-center >
				
					<router-view>
					</router-view>
					
				</v-container>
			</v-sheet>
			
		</v-main>
		
		<!-- <Nav ></Nav> -->
		<v-navigation-drawer
	       v-model="drawer"
	       app
	       right
	     >
	       <v-list dense>
	        <v-list-item two-line>
	          <v-list-item-avatar>
	            <img src="https://randomuser.me/api/portraits/women/81.jpg">
	          </v-list-item-avatar>
	
	          <v-list-item-content>
	            <v-list-item-title>Jane Smith</v-list-item-title>
	            <v-list-item-subtitle>Logged In</v-list-item-subtitle>
	      	  </v-list-item-content>
	      </v-list-item>
	         <v-list-item link>
	           <v-list-item-action>
	             <v-icon>mdi-home</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content>
	             <v-list-item-title>Home</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	 
	         <v-list-item link>
	           <v-list-item-action>
	             <v-icon>mdi-email</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content>
	             <v-list-item-title>Contact</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	         
	         <v-list-item @click="logout" v-if="isLogin">
	           <v-list-item-action>
	             <v-icon>mdi-logout</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content>
	             <v-list-item-title>로그아웃</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	       </v-list>
	     </v-navigation-drawer>
		
		<v-bottom-navigation
	      fixed 
	      dense 
	      app
	      scroll-target="#scroll-area-1"
     	  hide-on-scroll
	    >
	      <v-btn text color="deep-purple accent-1"
	       @click="$router.push({name : 'showOffList'})">
	        <v-icon>mdi-paw</v-icon>
	      </v-btn>
	
	      <v-btn text color="deep-purple accent-4">
	        <v-icon>mdi-heart</v-icon>
	      </v-btn>
	      
	      <v-btn text color="deep-purple accent-4"
	       @click="$router.push({name : 'AddShowOff'})">
	        <v-icon>mdi-plus-box-outline</v-icon>
	      </v-btn>
	
	      <v-btn text color="deep-purple accent-4">
	        <v-icon>mdi-map-marker</v-icon>
	      </v-btn>
	      <v-btn 
	      text color="deep-purple accent-4" 
	      @click="$router.push({path : '/myPage/'+userId})">
	        <v-icon>mdi-account</v-icon>
	      </v-btn>
	      <!-- <v-btn text color="deep-purple accent-4">
	        <span>Nearby</span>
	        <v-icon>mdi-cat</v-icon>
	      </v-btn> -->
	    </v-bottom-navigation>
	</div>
	
	
</template>

<script>
import Nav from '@/components/layout/Nav.vue'
import store from '@/store/store'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'
import {mapGetters , mapActions} from 'vuex'

var reqUrl =  window.location.pathname;

export default {
  name: 'DefaultLayout',
  components: {
	  //Nav,
  },
  data: () => ({
	  drawer : null,
	  myInfo : {},
	 /*isLogin : false,*/
  }),
  created : function(){
	  var that = this;
	  //that.isLogin = this.$store.getters.getIsLogin;
	  /* var that = this;
	  var xAxios = getAuthAxios();
	  xAxios.get('/api/validAuth')
	  .then(function(resp){
		  that.isLogin = true;
	  })
	  .catch(function(err){
		  that.isLogin = false;
	  }) */
  },
  computed : {
	...mapGetters({
		mbrInfo : 'getMbrInfo',
		isLogin : 'getIsLogin',
		userId  : 'getMbrId'
	})
	
  },
  methods : {
	  logout : function(){
		var that = this;
		if(confirm('로그아웃 하시겠습니까?')){
			store.dispatch('logout')
		}
	  }
  },
  mounted : function() {
	  var that = this;
  }
};
</script>
