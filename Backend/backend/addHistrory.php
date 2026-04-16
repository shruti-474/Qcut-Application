<?php
require_once("databaseconnect.php");
    $shopName      = $_POST['shopName'] ?? '';
    $shopAddress   = $_POST['shopAddress'] ?? '';
    $shopEmail     = $_POST['shopEmail'] ?? '';
    $finalName     = $_POST['finalUserName'] ?? '';
    $finalEmail    = $_POST['finalEmail'] ?? '';
    $finalDate     = $_POST['finalDate'] ?? '';
    $finalTime     = $_POST['finalTime'] ?? '';
    $finalService  = $_POST['services'] ?? '';
    $totalAmount   = $_POST['totalAmount'] ?? '';
    $paymentMode   = $_POST['paymentMode'] ?? 'Cash';
    $paymentStatus = $_POST['paymentStatus'] ?? 'Pending';
    $Fstatus = $_POST['Astatus'] ?? '';

     $query = "INSERT INTO history 
                  (shopname, shopaddress, shopemail, finalname, finalemail, finaldate, finaltime, finalservice, totalamount, paymentMode, paymentStatus, Appoinmentstatus) 
                  VALUES 
                  ('$shopName', '$shopAddress', '$shopEmail', '$finalName', '$finalEmail', '$finalDate', '$finalTime', '$finalService', '$totalAmount', '$paymentMode', '$paymentStatus','$Fstatus')";

                  $result = mysqli_query($connection,$query);

if ($result > 0) {
	$respond['success']=1;
}
else{
	$respond['success']=0;
}
echo json_encode($respond);
?>


