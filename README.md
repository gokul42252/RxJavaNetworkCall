<h1>Network API request Using Rxjava android tutorial Beginner</h1>
Checkout the previous post <a href="http://thoughtnerds.com/reactive-hello-world-application-android/">http://thoughtnerds.com/reactive-hello-world-application-android/</a>

for structure.

[amazon_link asins='1617293369,1784399108,9352134664,B071VG1VY9,B073L5JB27,1787120422,1484214293,1489138714,B0784QQV4W' template='ProductCarousel' store='200' marketplace='IN' link_id='3e407a04-34c3-11e8-9779-9bef6361f84b']
<h3 id="adding-rxjava-2-to-a-java-project">Adding RxJava 2 library  to a Java project in Android Studio</h3>
<div class="paragraph">

To use RxJava in a Gradle build, add the following as dependency.

</div>
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code data-lang="groovy">compile <span class="key">group</span>: <span class="string"><span class="delimiter">'</span><span class="content">io.reactivex.rxjava2</span><span class="delimiter">'</span></span>, <span class="key">name</span>: <span class="string"><span class="delimiter">'</span><span class="content">rxjava</span><span class="delimiter">'</span></span>, <span class="key">version</span>: <span class="string"><span class="delimiter">'</span><span class="content">2.1.1</span><span class="delimiter">'</span></span></code></pre>
</div>
</div>
<div class="paragraph">

For Maven, you can add RxJava via the following snippet.

</div>
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code data-lang="xml"><span class="tag">&lt;dependency&gt;</span>
    <span class="tag">&lt;groupId&gt;</span>io.reactivex.rxjava2<span class="tag">&lt;/groupId&gt;</span>
    <span class="tag">&lt;artifactId&gt;</span>rxjava<span class="tag">&lt;/artifactId&gt;</span>
    <span class="tag">&lt;version&gt;</span>2.0.4<span class="tag">&lt;/version&gt;</span>
<span class="tag">&lt;/dependency&gt;</span></code></pre>
</div>
</div>
<p name="f730" id="f730" class="graf graf--p graf-after--p">Since we are about to perform a network call, do not forget to add internet permission to your app<strong class="markup--strong markup--p-strong"><span> </span>manifest</strong>.</p>

<pre name="3025" class="graf graf--pre graf-after--p">&lt;<strong class="markup--strong markup--pre-strong">uses-permission android:name="android.permission.INTERNET" </strong>/&gt;

</pre>
<p name="2883" id="2883" class="graf graf--p graf-after--pre">1)Make sure you have a helper class for performing network calls. If you don’t have one, no worries. I got you covered. You can use this.</p>
<p name="50bf" id="50bf" class="graf graf--p graf-after--p"><strong class="markup--strong markup--p-strong">NetworkCall.java</strong></p>

<pre><span>package </span>com.cretlabs.rxjavaintroduction.utils<span>;
</span><span>
</span><span>
</span><span>import </span>android.util.Log<span>;
</span><span>
</span><span>import </span>java.io.BufferedReader<span>;
</span><span>import </span>java.io.IOException<span>;
</span><span>import </span>java.io.InputStream<span>;
</span><span>import </span>java.io.InputStreamReader<span>;
</span><span>import </span>java.net.HttpURLConnection<span>;
</span><span>import </span>java.net.URL<span>;
</span><span>
</span><span>
</span><span>public class </span>NetworkCall {

    <span>private int </span><span>responseCode</span><span>;
</span><span>
</span><span>    public </span>String <span>makeServiceCall</span>(String url){
        HttpURLConnection connection = <span>null;
</span><span>        </span>String response = <span>null;
</span><span>        </span>InputStream is = <span>null;
</span><span>
</span><span>        try </span>{
            URL imgUrl = <span>new </span>URL(url)<span>;
</span><span>            </span>connection = (HttpURLConnection) imgUrl.openConnection()<span>;
</span><span>            </span>connection.setRequestMethod(<span>"GET"</span>)<span>;
</span><span>            </span>connection.connect()<span>;
</span><span>            </span><span>responseCode </span>= connection.getResponseCode()<span>;
</span><span>            </span>is = connection.getInputStream()<span>;
</span><span>            </span>response = readIt(is)<span>;
</span><span>            </span>Log.<span>d</span>(<span>"response"</span><span>,</span>response)<span>;
</span><span>        </span>} <span>catch </span>(Exception e) {
            e.printStackTrace()<span>;
</span><span>        </span>} <span>finally </span>{
            <span>try </span>{
                <span>if </span>(is != <span>null</span>) {
                    connection.disconnect()<span>;
</span><span>                    </span>is.close()<span>;
</span><span>                </span>}
            } <span>catch </span>(Exception e) {
                e.printStackTrace()<span>;
</span><span>            </span>}
        }
        <span>return </span>response<span>;
</span><span>    </span>}

    <span>private </span>String <span>readIt</span>(InputStream iStream) <span>throws </span>Exception {
        String singleLine = <span>""</span><span>;
</span><span>        </span>StringBuilder totalLines = <span>new </span>StringBuilder(iStream.available())<span>;
</span><span>        </span>BufferedReader reader = <span>null;
</span><span>        try </span>{
            reader = <span>new </span>BufferedReader(<span>new </span>InputStreamReader(iStream))<span>;
</span><span>
</span><span>            while </span>((singleLine = reader.readLine()) != <span>null</span>) {
                totalLines.append(singleLine)<span>;
</span><span>            </span>}
        } <span>catch </span>(Exception e) {
            e.printStackTrace()<span>;
</span><span>        </span>}
        <span>finally </span>{
            <span>if </span>(reader != <span>null</span>) {
                <span>try </span>{
                    reader.close()<span>;
</span><span>                </span>} <span>catch </span>(IOException e) {
                    e.printStackTrace()<span>;
</span><span>                </span>}
            }
        }
        <span>return </span>totalLines.toString()<span>;
</span><span>    </span>}

}</pre>
2)Create an activity NetworkingActivity.java as Follows
<pre><span>package </span>com.cretlabs.rxjavaintroduction<span>;
</span><span>
</span><span>import </span>android.os.Bundle<span>;
</span><span>import </span>android.support.v7.app.AppCompatActivity<span>;
</span><span>import </span>android.support.v7.widget.AppCompatTextView<span>;
</span><span>import </span>android.util.Log<span>;
</span><span>
</span><span>import </span>com.cretlabs.rxjavaintroduction.utils.NetworkCall<span>;
</span><span>
</span><span>import </span>io.reactivex.Observable<span>;
</span><span>import </span>io.reactivex.ObservableEmitter<span>;
</span><span>import </span>io.reactivex.ObservableOnSubscribe<span>;
</span><span>import </span>io.reactivex.Observer<span>;
</span><span>import </span>io.reactivex.android.schedulers.AndroidSchedulers<span>;
</span><span>import </span>io.reactivex.disposables.Disposable<span>;
</span><span>import </span>io.reactivex.schedulers.Schedulers<span>;
</span><span>
</span><span>public class </span>NetworkingActivity <span>extends </span>AppCompatActivity {
    <span>private static final </span>String <span>TAG </span>= <span>"NetworkingActivity"</span><span>;
</span><span>    </span>NetworkCall <span>networkCall</span><span>;
</span><span>    </span>AppCompatTextView <span>mAppCompatTextView</span><span>;
</span><span>
</span><span>    </span><span>@Override
</span><span>    </span><span>protected void </span><span>onCreate</span>(Bundle savedInstanceState) {
        <span>super</span>.onCreate(savedInstanceState)<span>;
</span><span>        </span>setContentView(R.layout.<span>activity_networking</span>)<span>;
</span><span>        </span><span>mAppCompatTextView </span>= findViewById(R.id.<span>AN_txt</span>)<span>;
</span><span>        </span><span>networkCall </span>= <span>new </span>NetworkCall()<span>;
</span><span>        </span>Observer&lt;String&gt; networkCallObserver = <span>new </span>Observer&lt;String&gt;() {
            <span>@Override
</span><span>            </span><span>public void </span><span>onSubscribe</span>(Disposable d) {
                Log.<span>d</span>(<span>TAG</span><span>, </span><span>"onSubscribe() called with: d = [" </span>+ d + <span>"]"</span>)<span>;
</span><span>            </span>}

            <span>@Override
</span><span>            </span><span>public void </span><span>onNext</span>(String s) {
                Log.<span>d</span>(<span>TAG</span><span>, </span><span>"onNext() called with: s = [" </span>+ s + <span>"]"</span>)<span>;
</span><span>                </span><span>mAppCompatTextView</span>.setText(s)<span>;
</span><span>            </span>}

            <span>@Override
</span><span>            </span><span>public void </span><span>onError</span>(Throwable e) {
                Log.<span>d</span>(<span>TAG</span><span>, </span><span>"onError() called with: e = [" </span>+ e + <span>"]"</span>)<span>;
</span><span>            </span>}

            <span>@Override
</span><span>            </span><span>public void </span><span>onComplete</span>() {
                Log.<span>d</span>(<span>TAG</span><span>, </span><span>"onComplete() called"</span>)<span>;
</span><span>            </span>}
        }<span>;
</span><span>
</span><span>        </span>getDataFromServer().subscribeOn(Schedulers.<span>newThread</span>())
                .observeOn(AndroidSchedulers.<span>mainThread</span>())
                .subscribe(networkCallObserver)<span>;
</span><span>
</span><span>    </span>}

    <span>public </span>Observable&lt;String&gt; <span>getDataFromServer</span>() {
        <span>return </span>Observable.<span>create</span>(<span>new </span>ObservableOnSubscribe&lt;String&gt;() {
            <span>@Override
</span><span>            </span><span>public void </span><span>subscribe</span>(ObservableEmitter&lt;String&gt; emitter) <span>throws </span>Exception {
                <span>try </span>{
                    String response = <span>networkCall</span>.makeServiceCall(<span>"http://thoughtnerds.com/api/get_recent_posts/"</span>)<span>;
</span><span>                    </span>emitter.onNext(response)<span>;
</span><span>                </span>} <span>catch </span>(Exception e) {
                    emitter.onError(e)<span>;
</span><span>                </span>}
            }
        })<span>;
</span><span>    </span>}

}</pre>
3)In this we created a custom observer
<pre><span>public </span>Observable&lt;String&gt; <span>getDataFromServer</span>() {
    <span>return </span>Observable.<span>create</span>(<span>new </span>ObservableOnSubscribe&lt;String&gt;() {
        <span>@Override
</span><span>        </span><span>public void </span><span>subscribe</span>(ObservableEmitter&lt;String&gt; <span>emitter</span>) <span>throws </span>Exception {
            <span>try </span>{
                String response = <span>networkCall</span>.makeServiceCall(<span>"http://thoughtnerds.com/api/get_recent_posts/"</span>)<span>;
</span><span>                </span><span>emitter</span>.onNext(response)<span>;
</span><span>            </span>} <span>catch </span>(Exception e) {
                <span>emitter</span>.onError(e)<span>;
</span><span>            </span>}
        }
    })<span>;
</span>}</pre>
4)After Creating function subscribe the Observable from OnCreate
<pre>getDataFromServer().subscribeOn(Schedulers.<span>newThread</span>())
        .observeOn(AndroidSchedulers.<span>mainThread</span>())
        .subscribe(networkCallObserver)<span>;</pre>

Thus we can get response in <strong>OnNext</strong> in the </span><strong>networkCallObserver</strong> and set data to a textView

<img src="http://thoughtnerds.com/wp-content/uploads/2018/03/WhatsApp-Image-2018-02-18-at-10.40.44-PM-169x300.jpeg" alt="Network API request Using Rxjava android tutorial Beginner" width="319" height="566" class=" wp-image-936 aligncenter" />

[amazon_link asins='B073L5JB27,B071VG1VY9,9352134664,1787120422,1784399108,1617293369,B0784QQV4W,1489138714,1484214293' template='ProductCarousel' store='200' marketplace='IN' link_id='0befa111-34c7-11e8-a19e-036de9469172']

Thanks Happy Coding :D

<strong>


</strong></pre>
<pre><span>
 </span></pre>
