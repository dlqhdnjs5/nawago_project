<template>
	 <v-app>
	
		<v-app-bar fixed dense app >
		<v-btn icon @click="back">
			<v-icon>mdi-arrow-left</v-icon>
		</v-btn> 
		<v-toolbar-title style="color:#F48FB1;" @click="$router.push({name : 'showOffList'})">
			<img src="@/assets/logo/smallLogoPink2.png"  height="45px" style="padding-top:10px;" ><!-- NawaGo -->
		</v-toolbar-title>
		<v-spacer></v-spacer>
		<v-toolbar-items class="hidden-sm-and-down">
		</v-toolbar-items>
		<v-spacer></v-spacer>
		</v-app-bar>
		<v-main>
		
	    <v-container fill-height  style="max-width : 80%;" >
	
	      <router-view>
	      
	      </router-view>
	      
	    </v-container>
	    </v-main>
	
	</v-app>
</template>

<script>
import router from '@/router/index.js'

export default {
  name: 'PlainLayout',
  components: {
  },
  data: () => ({
    //
  }),
  created : function(){
	  
	  
  },
  methods : {
	  back : function(){
		  router.go(-1);
	  }
  }
};
</script>
