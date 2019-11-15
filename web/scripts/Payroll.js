
var Payroll = (function(){
      
    return {
        
        init: function() {
            
            $('#resultarea').html( "jQuery Version: " + $().jquery );
            
        },
        
        clocking_info: function(result) {
            
            var s = "<p>";
            var message = "";
            var punchtypeid = result["punchtypeid"];
            
            if(punchtypeid === 1) {
                message = "clocing-in";
            }
            else if(punchtypeid === 0) {
                message = "clocking-out";
            }
            s += "Thank you for <strong>" + message + "<strong>.";
            s += "Your " + message + " id is " + result["id"] + "</p>";
            
            $("#resultarea").html(s);
            
        },
        
        submitTimeClock: function() {
            
            var that = this;
            
            if( $("#badgeid").val === "" || $("#terminalid").val === "" ) {
                alert("You must enter a search paramete! Please try again.");
                return false;
            }
            
            $.ajax({
                
                url: '/timeclock',
                method: 'POST',
                data: $("#insertform").serialize(),
                dataType: 'json',
                
                sucess: function(response) {
                    that.clocking_info(response);
                }
   
            });
    
            return false;
        }

    };          
        
});

