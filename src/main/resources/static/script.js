var error = document.getElementById("errorMessage");
var errorHeading = document.getElementById("errorHeading");
var errorDiv = document.getElementById("errorDiv");
errorDiv.style.display = "none";
errorHeading.innerHTML = "Error";
error.innerHTML = "You have requested to transfer more funds than you currently have. Please try again.";

var welcome = document.getElementById("welcomeMessage");

var xhr = new XMLHttpRequest();
xhr.open("GET", "/api/username");
xhr.addEventListener("load", addUsername);
xhr.send();

function addUsername(){
    welcome.innerHTML = "Welcome, " + (this.response);
}


var xhr = new XMLHttpRequest();
xhr.open("GET", "api/dashboard/");
/*Preparing request body */
xhr.addEventListener("load", addBalances)
xhr.send();


$("a.tooltipLink").tooltip();
function addBalances(){
    errorDiv.style.display = "none";
    var data = this.response;

    var inputs = document.getElementsByClassName("inputArea");
    for(var i = 0; i < inputs.length; i++){
        inputs.item(i).innerHTML = "";
    }

    if(data != null && this.status > 300){
        // console.log(this.status);
        errorDiv.style.display = "block";
        return;
    }
    errorHeading.innerHTML = "";
    var dataArray = JSON.parse(data);

    var table = document.getElementById("mainTable");

    while(table.firstChild){
        table.removeChild(table.firstChild);
    }

    var totalSum = 0;

    for(var i = 0; i < dataArray.length; i++){
        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        var td3 = document.createElement("td");
        var td4 = document.createElement("td");
        var td5 = document.createElement("td");
        var str = dataArray[i]['type'];
        str = str.charAt(0) + str.slice(1).toLowerCase();

        td1.innerHTML = str;
        td2.innerHTML = dataArray[i]['name'].replace(/_/g, " ");
        td3.innerHTML = '$' + formatNumber(dataArray[i]['balance']);
        totalSum += dataArray[i]['balance'];

        var btnDeposit = document.createElement("button");
        btnDeposit.setAttribute("type", "button");
        btnDeposit.setAttribute("class", "btn btn-outline-primary btn-sm");
        btnDeposit.innerHTML = "Deposit";
        btnDeposit.setAttribute("id", "account"+ dataArray[i]['id']);


        var btnWithdraw = document.createElement("button");
        btnWithdraw.setAttribute("type", "button");
        btnWithdraw.setAttribute("class", "btn btn-outline-warning btn-sm");
        btnWithdraw.innerHTML = "Withdraw";
        btnWithdraw.setAttribute("id", "account"+ dataArray[i]['id']);

        td4.appendChild(btnDeposit);
        td4.appendChild(btnWithdraw);

        var inputArea = document.createElement("input");
        inputArea.setAttribute("type", "text");
        inputArea.setAttribute("class", "form-control inputArea");
        inputArea.setAttribute("placeholder", "Enter amount...");

        td5.appendChild(inputArea);
        var trow = document.createElement("tr");
		
        trow.appendChild(td1);
        trow.appendChild(td2);
        trow.appendChild(td3);
        trow.appendChild(td4);
        trow.appendChild(td5);
        table.appendChild(trow);



        btnWithdraw.onclick = function(btnWithdraw){withdraw(btnWithdraw)};
        btnDeposit.onclick = function(btnDeposit){deposit(btnDeposit)};

    }

    var trow = document.createElement("tr");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");


    td1.innerHTML = "All";
    td2.innerHTML = "Available Balance";
    td3.innerHTML = '$' + formatNumber(totalSum);

    trow.appendChild(td1);
    trow.appendChild(td2);
    trow.appendChild(td3);
    table.appendChild(trow);


    history();

}

function getAccount(number){
    var xhr = new XMLHttpRequest();
    var URI = "account-api/accounts/" + number;


    xhr.open("GET", URI, false);
    xhr.send();
    return xhr.response;
}

function deposit(element){
    var data = getAccount(element.srcElement.id.charAt(element.srcElement.id.length-1));

    var amount = element.srcElement.parentElement.nextSibling.childNodes[0].value;
    var xhr = new XMLHttpRequest();
    var URI = "user-api/deposit/"+ amount;
    xhr.open("POST", URI);
    /*Preparing request body */
    xhr.addEventListener("load", addBalances)


    if (amount === null || isNaN(amount) || amount =="") {
        alert("Please enter a valid amount to deposit.")
        return; //break out of the function early

    }

    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.send(data);
}

function withdraw(element){
    var data = getAccount(element.srcElement.id.charAt(element.srcElement.id.length-1));

    var amount = element.srcElement.parentElement.nextSibling.childNodes[0].value;
    var xhr = new XMLHttpRequest();
    var URI = "user-api/withdraw/"+ amount;
    xhr.open("POST", URI);
    /*Preparing request body */
    xhr.addEventListener("load", addBalances)

    if (amount === null || isNaN(amount) || amount =="") {
        alert("Please enter a valid amount to withdraw.")
        return; //break out of the function early

    }

    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    xhr.send(data);
}


$(function () {
    $('[data-toggle="popover"]').popover()
});

function history(){

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "api/history");
    /*Preparing request body */
    xhr.addEventListener("load", addHistory);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    xhr.send();

}

function addHistory(){

    var data = this.response;
    var dataArray = JSON.parse(data);
    var historyDiv = document.getElementById("historyDiv");
    var table = document.getElementById("historyTable");
    while(table.firstChild){
        table.removeChild(table.firstChild);
    }

    var th = document.createElement("thead");
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");
    var td5 = document.createElement("td");
    td1.innerHTML = "Account";
    td2.innerHTML = "Transaction";
    td3.innerHTML = "Starting Balance";
    td4.innerHTML = "Ending Balance";
    td5.innerHTML = "Date";
    var trow = document.createElement("tr");
    trow.appendChild(td1);
    trow.appendChild(td2);
    trow.appendChild(td3);
    trow.appendChild(td4);
    trow.appendChild(td5);
    th.appendChild(trow);
    table.appendChild(th);


    for(var i = 0; i < dataArray.length; i++){

        var trow = document.createElement("tr");
        var td1 = document.createElement("td");
        var td2 = document.createElement("td");
        var td3 = document.createElement("td");
        var td4 = document.createElement("td");
        var td5 = document.createElement("td");


        td1.innerHTML = dataArray[i]['accountName'].replace(/_/g, " ");
        td2.innerHTML = dataArray[i]['type'];//.replace(/_/g, " ");
        td3.innerHTML = '$' + formatNumber(dataArray[i]['balanceBefore']);

        td4.innerHTML = '$' + formatNumber(dataArray[i]['balanceAfter']);
        td5.innerHTML = moment(dataArray[i]['createdDatetime']).format('MMMM Do YYYY, h:mm:ss a');
        //td5.innerHTML = '$' + formatNumber(dataArray[i]['balance']);

        trow.appendChild(td1);
        trow.appendChild(td2);
        trow.appendChild(td3);
        trow.appendChild(td4);
        trow.appendChild(td5);

        table.insertBefore(trow, table.firstChild);
    }


}

function formatNumber(num){
    return parseFloat(Math.round(num * 100) /100).toFixed(2);
}
function toggleHistory(){
    var table = document.getElementById("historyTable");
    table.setAttribute("class", "table table-striped table-dark");
    if(table.style.display == "none"){
        table.style.display = "table";
    }
    else{
        table.style.display = "none";
    }
}

