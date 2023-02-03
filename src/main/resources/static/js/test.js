$(document).ready(function() {
    $("#add_fields_placeholder").change(function()
    {   if($(this).val() == "nameCheck")
        {   console.log("nameCheck");
            $("#nameCheck101").show();
            $("#codeCheck101").hide();
            $("#emailCheck").hide();
        } else if($(this).val() == "codeCheck")
               {
                   $("#nameCheck101").hide();
                   $("#codeCheck101").show();
                   $("#emailCheck").hide();
               }  else if($(this).val() == "emailCheck") {
                         $("#nameCheck101").hide();
                         $("#codeCheck101").hide();
                         $("#emailCheck101").show();
                }
   });
                              //$("#add_fields_placeholderValue").hide();
});

