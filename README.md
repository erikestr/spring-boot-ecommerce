# spring-boot-ecommerce

STRIPE INTEGRATION
1.	Add maven dependency
https://mvnrepository.com/artifact/com.stripe/stripe-java
2.	Config Stripe API key
a.	application.properties
b.	add: stripe.key.secret=…
3.	create dto PayInfo to save int amount, string currency
4.	add CheckoutService PaymentIntent createPaymentIntent(PaymentInfo paymentInfo) throws StripeException;
5.	impl createPaymentIntent()
6.	add Constructor @Value("${stripe.key.secret}") String secretKey
7.	add Constructor /* initialize stripe API with secret key */ Stripe.apiKey = secretKey;
8.	add createPaymentIntent()        ------->
List<String> paymentMethodTypes = new ArrayList<>();
paymentMethodTypes.add("card");
Map<String, Object> params = new HashMap<>();       
params.put("amount", paymentInfo.getAmount());       
params.put("currency", paymentInfo.getCurrency());
params.put("payment_method_types", paymentMethodTypes);
return PaymentIntent.create(params);
9.	update CheckoutController
