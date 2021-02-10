<template>
	<div class="overflow-hidden" >
		<v-app-bar fixed dense app >
		<v-btn icon @click="back">
			<v-icon>mdi-arrow-left</v-icon>
		</v-btn> 
		 <v-toolbar-title style="color:#00BFA5;" @click="$router.push({name : 'showOffList'})">
		  		<img src="@/assets/logo/smallLogoGreen2.png"  height="60px" style="padding-top:10px;" >
		  </v-toolbar-title>
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
	        <!-- <v-list-item two-line>
	          <v-list-item-avatar>
	            <img src="https://randomuser.me/api/portraits/women/81.jpg">
	          </v-list-item-avatar>
	
	          <v-list-item-content>
	            <v-list-item-title>Jane Smith</v-list-item-title>
	            <v-list-item-subtitle>Logged In</v-list-item-subtitle>
	      	  </v-list-item-content>
	      	</v-list-item>  -->
	         <v-list-item link>
	           <v-list-item-action>
	             <v-icon color="#FFA726">mdi-home</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content @click="$router.push({
	           							name:'showOffList'
	           						})"
	           >
	             <v-list-item-title>Home</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	 
	         <v-list-item link>
	           <v-list-item-action>
	             <v-icon color="#BBDEFB">mdi-email</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content @click="contactMe">
	             <v-list-item-title>Contact</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	         
	         <v-list-item @click="logout" v-if="isLogin">
	           <v-list-item-action>
	             <v-icon color="#F48FB1">mdi-logout</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content>
	             <v-list-item-title>로그아웃</v-list-item-title>
	           </v-list-item-content>
	         </v-list-item>
	         <v-list-item  v-else @click="$router.push({name:'plainLogin'})">
	           <v-list-item-action>
	             <v-icon color="#00BFA5">mdi-login</v-icon>
	           </v-list-item-action>
	 
	           <v-list-item-content>
	             <v-list-item-title>로그인</v-list-item-title>
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
	      <v-btn icon  color="#00BFA5"
	       @click="$router.push({name : 'showOffList'}).catch(()=>{})">
	        <v-icon>mdi-history</v-icon><!-- <v-icon>mdi-paw</v-icon> -->
	      </v-btn>
	
	      <v-btn icon  color="#F48FB1">
	        <v-icon>mdi-heart</v-icon>
	      </v-btn>
	      
	      <v-btn icon  color="#00BFA5"
	       @click="$router.push({name : 'AddShowOff'}).catch(()=>{})">
	        <v-icon>mdi-plus-box-outline</v-icon>
	      </v-btn>
	
	      <v-btn icon  color="#00BFA5"
	      	@click="$router.push({
	      		name : 'abandonedPet'
	      	}).catch(()=>{})"
	      >
	        <v-icon>mdi-paw</v-icon>
	      </v-btn>
	      <v-btn 
	      icon
	       color="#00BFA5" 
	      @click="goMyPage(isLogin)">
	        <v-icon>mdi-account</v-icon>
	      </v-btn>
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
  }),
  beforeCreate(){
	this.$store.commit('getPublicMbrInfo')
  },
  created : function(){
	  var that = this;
  },
  computed : {
	...mapGetters({
		mbrInfo : 'getMbrInfo',
		isLogin : 'getIsLogin',
	})
	
  },
  methods : {
	  contactMe : function(){
		if(confirm('문의 요청 sms를 보내시겠습니까?'))document.location.href= "sms:01020340448"
	  },
	  logout : function(){
		var that = this;
		if(confirm('로그아웃 하시겠습니까?')){
			store.dispatch('logout')
		}
	  },
	  back : function(){
		  this.$router.go(-1);
	  },
	  goMyPage : function(isLoginParam){
		  var that = this;
		  if(!isLoginParam){
			  alert('로그인이 필요한 화면입니다.\n로그인 페이지로 이동합니다.');
			  that.$router.push({name: 'plainLogin'});
		  }else{
			  var loginId = that.$store.getters.getMbrId;
			  that.$router.push({path:'/user/'+loginId}).catch(()=>{});
		  }
	  }
	  
  },
  mounted : function() {
	  var that = this;
  }
};
</script>
