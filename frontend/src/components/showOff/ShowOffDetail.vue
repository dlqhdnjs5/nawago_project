<template>
<div>
</div>
</template>

<script>
import { Swiper, SwiperSlide, directive } from 'vue-awesome-swiper'

  export default {
    name: 'ShowOffDetail',
    components : {
    	Swiper,
    	SwiperSlide,
    },
    directives: {
        swiper: directive
    },
   	data : function(){
   		return {
   			swiperOptions: {
             slidesPerView: 1,
   	          pagination: {
   	            el: '.swiper-pagination',
   	          },
   			},
   		}
   	},
   	computed: {
        swiper() {
          return this.$refs.mySwiper.$swiper
        }
    },
   	created : function(){
   	},
   	mounted : function() {
   	},
   	methods : {
   	 	bottomVisible : function(){
         const scrollY = window.scrollY
         const visible = document.documentElement.clientHeight
         const pageHeight = document.documentElement.scrollHeight - 110
         const bottomOfPage = visible + scrollY >= pageHeight
         return bottomOfPage || pageHeight < visible
   	 	},
   		
   	}
  }
</script>
