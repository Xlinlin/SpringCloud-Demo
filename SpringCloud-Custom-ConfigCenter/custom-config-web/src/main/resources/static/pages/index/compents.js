App.register({
    template:'#tags-nav',
    data :function() {
        return {
            tagBodyLeft: 0,
            rightOffset: 45,
            outerPadding: 4,
            light:2
        }
    },
    name: 'TagsNav',
    props: ['list','value'],
    methods: {
        handleMouseScroll :function(e) {
            let type = e.type;
            let delta = 0;
            if (type === 'DOMMouseScroll' || type === 'mousewheel') {
                delta = (e.wheelDelta) ? e.wheelDelta : -(e.detail || 0) * 40
            }
            this.handleScroll(delta)
        },
        handleScroll :function(offset) {
            const outerWidth = this.$refs.scrollOuter.offsetWidth
            const bodyWidth = this.$refs.scrollBody.offsetWidth
            if (offset > 0) {
                this.tagBodyLeft = Math.min(0, this.tagBodyLeft + offset)
            } else {
                if (outerWidth < bodyWidth) {
                    if (this.tagBodyLeft < -(bodyWidth - outerWidth)) {
                        this.tagBodyLeft = this.tagBodyLeft
                    } else {
                        this.tagBodyLeft = Math.max(this.tagBodyLeft + offset, outerWidth - bodyWidth)
                    }
                } else {
                    this.tagBodyLeft = 0
                }
            }
        },
        handleClose :function(e,menuId) {
            console.log(menuId)
            this.$emit('on-close', 'single',menuId);
        },
        handleClick :function(item,e) {
            if ( e.target.tagName ==  'I'){
                this.handleClose(e,item.menuId);
            }else{
                this.value = item.menuId
                this.$emit('input', item)
            }
        },
        handleTagsOption :function(type){
            this.$emit('on-close', type);
        },
        moveToHome:function(){
            this.tagBodyLeft = 0;
        },
        moveToView :function(tag) {

            let outerWidth = $(this.$refs.scrollOuter).outerWidth();
            let bodyWidth = $(this.$refs.scrollBody).outerWidth();
            var tagPos = $(tag).position();
            var tagleft = $(tag).position().left + $(tag).outerWidth() + 10;
            var visableLeft = tagleft + this.tagBodyLeft;
            var marginLeft = outerWidth-tagleft;
            if ( tagleft < outerWidth ){
                this.tagBodyLeft = 0;
                return;
            }
            //判断是否在可视范围内
            if ( visableLeft < outerWidth && visableLeft > 0 ){
                //不移动
            }else{
                if ( marginLeft < 0 ){
                    this.tagBodyLeft = marginLeft;
                }else{
                    this.tagBodyLeft = 0;
                }
            }
        }
    }
});

Vue.component('ue',{
    template:'<div style="width:100%;"><script :id="\'UEDITOR_\'+eid" type="text/plain" style="width:1024px;height:400px;" ></script>  </div>',
    data () {
        return {
            editor: null
        }
    },
    props: {
        eid:{
            type: String,
        },
        content: {
            type: String,
            default:""
        },
        config: {
            type: Object,
            default: {
                initialFrameWidth:1000,
                initialFrameHeight:500
            }
        }
    },
    mounted() {
        const _this = this;
        this.editor = UE.getEditor('UEDITOR_'+this.eid, this.config); // 初始化UE
        this.editor.addListener("ready", function () {
            _this.editor.setContent(_this.content); // 确保UE加载完成后，放入内容。
        });
    },
    methods: {
        getUEContent() { // 获取内容方法
            return this.editor.getContent()
        }
    },
    destroyed() {
        this.editor.destroy();
    }
})
