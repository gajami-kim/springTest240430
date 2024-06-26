console.log("board modify in");

document.addEventListener('click',(e)=>{
    console.log(e.target);
    if(e.target.classList.contains('fileDelBtnC')){
        let uuid  = e.target.dataset.uuid;
        let bno = e.target.dataset.bno;
        console.log(uuid);

        removeFileFromServer(uuid,bno).then(result=>{
            if(result==1) {
                alert("파일 삭제 완료");
                e.target.closest('li').remove();
            }
        })
    }
})

//비동기 메서드 맵핑방법 : post, get, put, delete
async function removeFileFromServer(uuid, bno) {
    try {
        const url = "/board/"+uuid+"/"+bno;
        const config = {
            method:'delete'
        }

        const resp = await fetch(url, config);
        const result = resp.text();
        return result;
    } catch (error) {
        console.log(error);
    }
}