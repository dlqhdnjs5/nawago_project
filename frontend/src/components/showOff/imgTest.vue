<template>
    <div class="mini-group6 v2">
        <div class="evt-insta">
            <div class="group-copy">
                <p class="head-copy">글라이드 공식 인스타그램에서<br>글라이드 소식을 먼저 확인하세요</p>
                <p class="copy-desc">공식 계정 팔로우 하시면 다양한 이벤트와 <br>
                    글라이드 소식들을 만나실 수 있어요
                </p>
                <template v-for="(detailSet, index) in cornerDetailList">
                  <template v-for="dspCnrConttText in detailSet.dspCnrConttTextList" >
                    <template v-if="dspCnrConttText.dspCnrContt.conttCnncUrl != null">
                      <a href="javascript:void(0); return false;" class="ico-arrow-right16"  @click="openUrl(dspCnrConttText.dspCnrContt.conttCnncUrl)">바로가기</a>
                    </template>
                  </template>
                </template>
                <div class="fileDiv-wrap">
                	<div v-for="(fileDiv ,idx) in fileList">
	                	<input :id="'fileId' + idx" type="file"	@input="upload" /> <button @click="deleteFile(idx)" style="display:none;">x</button>
                	</div>
                </div>
                <div>
<!--                 	<button @click="addFile">추가</button> -->
                </div>
                

            </div>
        </div>
    </div>
</template>
<script>
 module.exports = {
        name: 'imgTest',
        /* ${template} */
        props: ['cornerDetail', 'cornerDetailList'],
        components: {
        },
        computed: {

        },
        data: function(){
            return {
            	fileList : 1,
            	hey : 'hey'
            }
        },
        mounted: function(){
        },
        methods: {
          openUrl : function(url){
            //window.location.href='http://'+url; 3.2v 에서 새창으로 열리도록 수정
            window.open('http://'+url);
          },
          addFile : function() {
        	  console.log('aas')
        	  var that = this;
        	  
        	  that.fileList ++;
        	  console.log(that.hey);
        	  console.log(that.fileList);
          },
          upload : function(event) {
        	  
        	  var that  = this;
        	  console.log('upload')
        	  console.log(event.target.files);
        	  
        	  that.showDeleteBtn(event.target);
        	  that.createBtn();
        	  
          },
          deleteFile : function(idx){
        	  var that = this;
        	  var $el = document.querySelector('#fileId'+idx).parentElement;
        	  var count = document.querySelector('.fileDiv-wrap').childElementCount;

        	  if(count == 1){
        		  document.querySelector('#fileId'+idx).value = null;
        	  }else{
        		  
        	  }
        	  
        	  $el.remove();
        	  that.createBtn();
        	
          },
          createBtn : function(){
        	  var that = this;
        	  var emptyFileCount = 0;
        	  var count = document.querySelector('.fileDiv-wrap').childElementCount;
        	  
			  var fileDivList = document.querySelector('.fileDiv-wrap').children;
			 
        	  console.log('list : ',fileDivList[0])
        	  
        	  for(var i = 0 ; i < fileDivList.length ; i ++){
        		  console.log(fileDivList[i].firstChild.files)
        		  if(fileDivList[i].firstChild.files.length == 0)emptyFileCount++;
        	  }
        	  
        	  if(count < 3 && emptyFileCount < 1) that.fileList ++;
        	  
          },
          showDeleteBtn : function(el){
        	  el.nextElementSibling.style.display = 'block';
          }
        }
    };
</script>
