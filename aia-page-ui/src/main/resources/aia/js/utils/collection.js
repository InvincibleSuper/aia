

function newArrayList(){
    return {
        items:new Array(10),
        size:0,
        add:function (item){
          this.addIndex(this.size,item)
        },
        addIndex:function (index,item){
            this.ensureCapacity(this.size+1)
            for (let i = this.size; i > index ; i--) {
                this.items[i] = this.items[i-1]
            }
            this.size ++;
            this.items[index] = item;
        },
        ensureCapacity:function (count){
            if (count >this.size){
                var newItems = new Array(this.size>>1);
                for (let i = 0; i <= this.size; i++) {
                    newItems[i] = this.items[i]
                }
                this.items = newItems;
            }
        },
        remove:function (index){
            if (index == this.items.length - 1){
                this.items[index] = null
            }else{
                for (let i = index; i < this.size; i++) {
                    this.items[i] = (this.items[i+1])
                }
            }
            this.size --;
        },
        get:function (index){
            if (index <0 || index >= this.size)return null;
            return this.items[index];
        }


    }
}

export default {newArrayList}