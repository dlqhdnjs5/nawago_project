<template>
  <v-container>
         <v-layout class="text-center">
         	<v-flex xs12 sm12 md12 lg12 xl12>
	 			<img src="@/assets/logo/fullLogo4Green2.png" width="240px">
	 			<br>
	 			<br>
	 			<h3 >요청하신 페이지를  찾지 못했습니다</h3>
	 			<v-btn style="margin-top : 10%"
	 				@click="$router.push({
	 				name : 'showOffList'
	 				})"
	 			>
	 				메인 페이지로 이동
	 			</v-btn>
	 		</v-flex>
         </v-layout>
    
  </v-container>
</template>

<script>
  export default {
    name: 'ErrorPage404',
    created : function(){
    	
    }
  }
</script>
