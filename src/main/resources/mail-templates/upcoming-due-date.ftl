<html>
<body>
<h3>Hello, ${borrowRecord.reader.name}.</h3>

<div>
    You have ${daysRemain} <#if daysRemain == 7>days<#else>day</#if> left to return "${borrowRecord.book.title}".
    Thank you for using our library.
</div>
<span>To contact us you can reply on this email address.</span>
</body>
</html>