import Vue from 'vue'
import App from './App.vue'
import axios from './plugins/axios'
import vuetify from './plugins/vuetify';
import router from './router'
//import Vuex from 'vuex'
import store from '@/store/store'
import {getAuthAxios} from '@/interceptor/axiosInterceptor'
import VueAwesomeSwiper from 'vue-awesome-swiper'
import InfiniteLoading from 'vue-infinite-loading';
import VueMoment from 'vue-moment'
import moment from 'moment-timezone'
import 'swiper/css/swiper.css'


Vue.config.productionTip = false


Vue.use(axios)
Vue.use(store)
Vue.use(vuetify)
Vue.use(VueAwesomeSwiper)
Vue.use(InfiniteLoading)
Vue.use(VueMoment,{ moment, })


new Vue({
  directives: {InfiniteLoading},
  getAuthAxios,
 /* beforeCreate(){
	this.$store.commit('getPublicMbrInfo')
  },*/
  axios,
  vuetify,
  store,
  router,
  render: h => h(App)
}).$mount('#app')
