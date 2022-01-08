console.log("This is script file")

//function
const toggleSidebar= () =>{
    if( $('.sidebar').is(":visible") ){
        //show
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left","0%");

    }else{
        //hide
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left","20%");
    }
}

