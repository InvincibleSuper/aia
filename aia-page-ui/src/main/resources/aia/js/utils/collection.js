

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
            for (let i = index; i < this.size-1; i++) {
                this.items[i] = (this.items[i+1])
            }
            this.size --;
        },
        get:function (index){
            return this.items[index];
        }


    }
}

