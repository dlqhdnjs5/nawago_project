import Vue from 'vue'
import App from './App.vue'
import axios from './plugins/axios'
import vuetify from './plugins/vuetify';
import router from './router'
//import Vuex from 'vuex'
import store from '@/store/store'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import "swiper/swiper-bundle.min.css"
import InfiniteLoading from 'vue-infinite-loading';

Vue.config.productionTip = false


Vue.use(axios)
Vue.use(store)
Vue.use(vuetify)
Vue.use(VueAwesomeSwiper)
Vue.use(InfiniteLoading)

new Vue({
  directives: {InfiniteLoading},
  getAuthAxios,
  beforeCreate(){
	console.log('beforeCreated')
	this.$store.commit('getPublicMbrInfo')
	/*.then(function(resp){
		console.log('beforeCreated resp : ',err)
	})
	.catch(function(err){
		console.log('beforeCreated err : ',err)
	})*/
  },
  axios,
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
