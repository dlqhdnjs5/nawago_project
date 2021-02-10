<template>
<div>
	<v-layout row wrap>
		<v-expansion-panels>
			<v-expansion-panel>
		      <v-expansion-panel-header>
		        	<h3>유기 동물 친구들을 식구로 맞이해요 </h3>
		      </v-expansion-panel-header>
		      <v-expansion-panel-content >
		       <v-flex >
		       		가족을 잃은 상처를 따듯하게 감싸주세요  <v-icon color ="#F48FB1"> mdi-hand-heart </v-icon>
		       </v-flex>
		       <v-flex class="caption">
		       	<span class="font-weight-black" >출처 : </span>농림축산검역본부 동물보호과
		       </v-flex>
		      </v-expansion-panel-content>
		    </v-expansion-panel>
		    </v-expansion-panels>
		<v-flex xs12 sm6 md4 lg4 xl4  v-for="(abandonedPet,idx) in abandonedPetList">
		  <v-card
		    class="mx-auto"
		    max-width="344"
		  >
		    <v-img
		      height="344"
		      :src="abandonedPet.popfile"
		    ></v-img>
		
		    <v-card-title >
		      {{abandonedPet.kindCd}}
				<v-icon  color="#F48FB1" v-if="abandonedPet.sexCd == 'F'">mdi-gender-female</v-icon>
				<v-icon  color="#81D4FA" v-else>mdi-gender-male</v-icon>
		    </v-card-title>
		
		    <v-card-subtitle v-html="'발견 지역 : ' + abandonedPet.happenPlace">
		    </v-card-subtitle>
		     <v-expansion-panels>
			<v-expansion-panel>
		      <v-expansion-panel-header>
		        	자세히
		      </v-expansion-panel-header>
		      <v-expansion-panel-content class="caption">
		       <v-flex class="caption">
		       		<span class="font-weight-bold" >중성화 여부  :</span>   
		       		<span v-if="abandonedPet.neuterYn == 'Y'">
		       			중성화
		       		</span>
		       		<span v-else>
		       			안함
		       		</span>
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >나이  :</span>  {{abandonedPet.age}}
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >체중  :</span>  {{abandonedPet.weight}}
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >특징  :</span>  {{abandonedPet.specialMark}}
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >상태  :</span>  {{abandonedPet.processState}}
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >보호소 이름  :</span>  {{abandonedPet.careNm}}
		       </v-flex>
		       <v-flex>
		       		<span class="font-weight-black" >보호소 전화번호  :</span>   {{abandonedPet.careTel}}
		       </v-flex>
		       
		      </v-expansion-panel-content>
		    </v-expansion-panel>
		    </v-expansion-panels>
		  </v-card>
		  </v-flex>
	</v-layout>
	<div class="text-center">
		<v-layout v-if="loading" xs12 sm12 md12 lg12 xl12 >
			<v-flex>
				<v-progress-circular
			      indeterminate
			      color="#F48FB1"
			    ></v-progress-circular>
			</v-flex>
		</v-layout>
	</div>
	
</div>
	
</template>

<script>
import InfiniteLoading from 'vue-infinite-loading'
import { getAuthAxios , getAuthCheckedAxios} from '@/interceptor/axiosInterceptor'
import store from '@/store/store'
/*  import ShowOffReplyComponent from '@/components/showOff/ShowOffReplyComponent'*/

  export default {
    name: 'AbandonedPet',
    components : {
    },
   	data : function(){
   		return {
   			show: false,
   			currentPage : 1,
   			abandonedPetList : [],
   			bottom: false,
   			loading : false,
   		}
   	},
    beforeCreate(){
	},
   	created : function(){
		var that = this;
		that.getFirstAbandonedPetListPage();
   	},
   	mounted : function() {
   		var that = this;
   		window.addEventListener('scroll', function(){
   			that.bottom = that.bottomVisible()
        });
   	},
   	watch: {
   	    bottom(bottom) {
   	      var that =this;
   	      if (bottom) {
   	    	that.getNextAbandonedPetListPage();
   	      }
   	    },
   	 },
   	methods : {
   	 	bottomVisible : function(){
         const scrollY = window.scrollY
         const visible = document.documentElement.clientHeight
         const pageHeight = document.documentElement.scrollHeight - 110
         const bottomOfPage = visible + scrollY >= pageHeight
         return bottomOfPage || pageHeight < visible
         
		},
		getFirstAbandonedPetListPage: function(){ //처음 인입시 리스트 로드
   			
  			var that = this;
  	   		
  			this.$store.dispatch('PageGetter',{
  				url : '/api/abandoned/getAbandonedPetInfo',
  				page : that.currentPage
  			})
  			.then(resp =>{
  				that.abandonedPetList =  resp;
  			})
  			.catch(err => {
  				console.log("pageGetter error");
  			})
  			
		},
		getNextAbandonedPetListPage: function($state){//처음 이후 스크롤시  리스트 로드
			
   			var that = this;
			that.loading = true;
   			that.currentPage +=1;
   			this.$store.dispatch('PageGetter',{
   				url : '/api/abandoned/getAbandonedPetInfo',
   				page : that.currentPage
   			})
   			.then(resp =>{
   				setTimeout(function(){
   					if(resp.length > 0 ){
   						var mergeList = that.abandonedPetList.concat(resp);
   						that.abandonedPetList = mergeList;
   						that.loading = false;
   					}else{
   						that.loading = false;
   					}
   				},1000);
   			})
   			.catch(err => {
   				console.log("pageGetter error");
   			})
   			
		},
   		timeForToday : function(value) {
   			
   			var that = this;
   			var today = this.$moment.tz('Asia/Seoul');
	        var timeValue = this.$moment(value).tz('Asia/Seoul'); 
	        var betweenTime = Math.floor((today - timeValue) / 1000 / 60);
	       
	        if (betweenTime < 1) return '방금전';
	        if (betweenTime < 60) {
	            return parseInt(betweenTime) + '분전';
	        }
	        
	        var betweenTimeHour = Math.floor(betweenTime / 60);
	        if (betweenTimeHour < 24) {
	            return parseInt(betweenTimeHour) + '시간전';
	        }
	        
	        var betweenTimeDay = Math.floor(betweenTime / 60 / 24);
	        if (betweenTimeDay < 365) {
	            return parseInt(betweenTimeDay) + '일전';
	        }
	        
	        return Math.floor(betweenTimeDay / 365) + '년전';

   		},
   		getShoOffCont : function(shoOffCont){
			return shoOffCont.replaceAll('\n','</br>');
   		}
   		
   	}
  }
</script>
