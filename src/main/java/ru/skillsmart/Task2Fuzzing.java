package ru.skillsmart;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task2Fuzzing {
    /*
    Несмотря на старания, реализация у меня получилась посредственной, к сожалению.
    Повыбирав между имевшимися вариантами, я взял jazzer, и хотел его применить на своем учебном проекте (автопарк).
    Сперва я попробовал запускать его через командную строку, но ему постоянно не хватало каких-то путей, чтобы запуститься, и я решил попробовать добавить его как зависимость в проект, и создать тесты с Jazzer.
    Но что выбрать? Запускать fuzz-тесты на форме логина и контроллера, пожалуй, в это приложении не было большого смысла, поэтому я выбрал один из методов, что не взаимодействует с базой, и написал простой тест (1 метод).
    Поработав 5 минут и прогнав 233275617 тестов, никаких ошибок найти не удалось.
    Интересно было взять что-то посложнее, я нашел библиотеку, которую писал на работе, и стал проверять её.
    Здесь это был уже не отдельный метод, а связка, поэтому всю конструкцию пришлось поднимать через @SpringBootTest.
    Метод - launcherLibraryFuzzTest, также никаких проблем не обнаружил. Но хотелось же все-таки обнаружить!
    Я взял еще одну рабочую библиотеку со сложной логикой, и хотел проверить её.
    Написанный метод - fuzzTestGenerateReportRequest.
    Я потратил уйму времени, но проверку осуществить мне так и не удалось. Сначала долго возился с зависимостями от другой библиотеки, подключенной к тестируемой, потом - из-за проблем с подключением к базе данных (при том, что само по себе приложение стартует и взаимодействует с базой нормально).
    В сухом остатке: мне довольно поверхностно удалось познакомиться с fuzz-тестированием, но рассчитывал я на большее.
    Инструмент интересный, честно говоря, раньше о нем вообще не слышал, и может принести пользу, если хорошо разобраться и правильно его применять.
    До возможности тестировать в CI-CD и в докере я пока не добрался. С другой стороны, я бы не стал бы добавлять стейдж fuzz-тестирования в пайплайн, его хорошо применять при разработке и проверки сложной логики, но в пайплайне это, на мой взгляд, избыточно.
    Буду в любом случае дальше разбираться и искать варианты применения, спасибо.
     */

    @FuzzTest
    public void myFuzzTest(FuzzedDataProvider data) {
        String param1 = data.consumeString(10);
        int param2 = data.consumeInt();
        double longitude = data.consumeDouble();
        double latitude = data.consumeDouble();
        double distance = data.consumeDouble();

        generationUtility.generateStartEndpoint(longitude, latitude, param2, distance);
    }

    /*

INFO: using inputs from: \\?\G:\My Drive\training\carpark\.cifuzz-corpus\ru.skillsmart.carpark.CarparkFuzzTest\myFuzzTest
INFO: using inputs from: C:\Users\A12671~1\AppData\Local\Temp\jazzer-java-seeds8782520181881099421
INFO: found LLVMFuzzerCustomMutator (00007FFEDF9B33D0). Disabling -len_control by default.
INFO: Running with entropic power schedule (0xFF, 100).
INFO: Seed: 888030670
INFO: Loaded 1 modules   (512 inline 8-bit counters): 512 [0000022EAB25E040, 0000022EAB25E240),
INFO: Loaded 1 PC tables (512 PCs): 512 [0000022EA6D7D7A0,0000022EA6D7F7A0),
INFO:        0 files found in \\?\G:\My Drive\training\carpark\.cifuzz-corpus\ru.skillsmart.carpark.CarparkFuzzTest\myFuzzTest
INFO:        0 files found in C:\Users\A12671~1\AppData\Local\Temp\jazzer-java-seeds8782520181881099421
INFO: -max_len is not provided; libFuzzer will not generate inputs larger than 4096 bytes
INFO: A corpus is not provided, starting from an empty corpus
#2	INITED cov: 12 ft: 12 corp: 1/1b exec/s: 0 rss: 946Mb
#2097152	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 1048576 rss: 947Mb
#4194304	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 1048576 rss: 947Mb
#8388608	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 932067 rss: 947Mb
#16777216	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 883011 rss: 947Mb
#33554432	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 883011 rss: 947Mb
#67108864	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 860370 rss: 947Mb
#134217728	pulse  cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 818400 rss: 947Mb
#233275617	DONE   cov: 12 ft: 12 corp: 1/1b lim: 4096 exec/s: 775002 rss: 947Mb
Done 233275617 runs in 301 second(s)

 */

    @FuzzTest
    public void launcherLibraryFuzzTest(FuzzedDataProvider data) {
        LaunchRequest launchRequest = new LaunchRequest();
        int launchId = data.consumeInt();
        launchRequest.setLaunchId(launchId);
        String callbackUrl = data.consumeString(300);
        launchRequest.setCallbackUrl(callbackUrl);
        launchRequest.setTestCaseName(callbackUrl);

        String key1 = data.consumeString(300);
        String key2 = data.consumeString(300);
        String key3 = data.consumeString(300);
        Long value1 = data.consumeLong();
        Long value2 = data.consumeLong();
        Long value3 = data.consumeLong();
        DataContainer dataContainer = DataContainer.hashMapContainer();
        Map<String, Object> testData = new HashMap<>();
        testData.put(key1, value1);
        testData.put(key2, value2);
        testData.put(key3, value3);
        dataContainer.getData().putAll(testData);
        launchRequest.setTestData(dataContainer);

        testController.execute(launchRequest);
    }

    /*
    2024-06-01 14:49:43.965 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.965 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.965 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.965 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.966 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.967 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
2024-06-01 14:49:43.967 DEBUG main d.t.c.l.c.TestController line 30  : Request received: LaunchRequest(launchId=0, testCaseName=, callbackUrl=, testData=DataContainer(data={=0}), predecessorTestData=null, reportMetadata=null)
#3561842	DONE   cov: 46 ft: 46 corp: 1/1b lim: 4096 exec/s: 11833 rss: 1207Mb
Done 3561842 runs in 301 second(s)
2024-06-01 14:49:43.970 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 18, missCount = 1, failureCount = 0]
2024-06-01 14:49:43.971 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 19, missCount = 1, failureCount = 0]
2024-06-01 14:49:43.971 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 20, missCount = 1, failureCount = 0]
2024-06-01 14:49:43.971 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 21, missCount = 1, failureCount = 0]
2024-06-01 14:49:43.972 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 22, missCount = 1, failureCount = 0]
2024-06-01 14:49:43.973 DEBUG main o.s.t.c.c.DefaultContextCache line 318  : Spring test ApplicationContext cache statistics: [DefaultContextCache@7a083b96 size = 1, maxSize = 32, parentContextCount = 0, hitCount = 23, missCount = 1, failureCount = 0]


     */

    @FuzzTest
    void fuzzTestGenerateReportRequest(FuzzedDataProvider data) {
        BugDetectors.allowNetworkConnections();

        String name = data.consumeString(100);

        int matchedStatusesSize = data.consumeInt(0, 10);
        List<String> matchedStatuses = new ArrayList<>();
        for (int i = 0; i < matchedStatusesSize; i++) {
            matchedStatuses.add(data.consumeString(50));
        }

        String messageRegex = data.consumeString(100);
        String traceRegex = data.consumeString(100);

        AllureCategories allureCategory = new AllureCategories(name);
        allureCategory.setMatchedStatuses(matchedStatuses);
        allureCategory.setMessageRegex(messageRegex);
        allureCategory.setTraceRegex(traceRegex);

        int environmentPropertiesSize = data.consumeInt(0, 10);
        Map<String, Object> environmentProperties = new HashMap<>();
        for (int i = 0; i < environmentPropertiesSize; i++) {
            environmentProperties.put(data.consumeString(50), data.consumeRemainingAsString());
        }

        String emailList = data.consumeString(200);
        String slackChannelsList = data.consumeString(200);
        String launchID = data.consumeString(50);
        String entityName = data.consumeString(100);
        Instant startTime = Instant.ofEpochSecond(data.consumeLong());
        String ns = data.consumeString(100);

        ReportNotificationOptions notificationOptions = new ReportNotificationOptions();
        notificationOptions.setNs(ns);
        notificationOptions.setEmailList(emailList);
        notificationOptions.setEntityName(entityName);
        notificationOptions.setStartTime(startTime);
        notificationOptions.setLaunchID(launchID);
        notificationOptions.setSlackChannelsList(slackChannelsList);

        GenerateReportRequest request = new GenerateReportRequest();
        request.setAllureCategories(List.of(allureCategory));
        request.setEnvironmentProperties(environmentProperties);
        request.setNotificationOptions(notificationOptions);

        String setId = data.consumeString(300);
        generateReportService.generate(setId, ns, request);
    }

}
