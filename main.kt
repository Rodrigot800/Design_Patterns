// Repositório de exemplos de Design Patterns em Kotlin.
// Cada seção abaixo representa um padrão e demonstra o problema que ele resolve,
// além do contexto de uso real e da solução prática.

// -----------------------------
// 1) Singleton
// -----------------------------
class DatabaseConnection private constructor() {
    fun query(sql: String): String = "Executando SQL: $sql"

    companion object {
        val instance: DatabaseConnection by lazy { DatabaseConnection() }
    }
}

// -----------------------------
// 2) Factory Method
// -----------------------------
interface Notification {
    fun send(message: String)
}

class EmailNotification : Notification {
    override fun send(message: String) {
        println("[Email] $message")
    }
}

class SmsNotification : Notification {
    override fun send(message: String) {
        println("[SMS] $message")
    }
}

class NotificationFactory {
    fun create(type: String): Notification {
        return when (type.lowercase()) {
            "email" -> EmailNotification()
            "sms" -> SmsNotification()
            else -> throw IllegalArgumentException("Tipo de notificação não suportado: $type")
        }
    }
}

// -----------------------------
// 3) Builder
// -----------------------------
data class House(
    val walls: Int,
    val doors: Int,
    val windows: Int,
    val garage: Boolean,
    val roof: String
)

class HouseBuilder {
    private var walls = 0
    private var doors = 0
    private var windows = 0
    private var garage = false
    private var roof = "Telha"

    fun setWalls(value: Int) = apply { walls = value }
    fun setDoors(value: Int) = apply { doors = value }
    fun setWindows(value: Int) = apply { windows = value }
    fun setGarage(value: Boolean) = apply { garage = value }
    fun setRoof(value: String) = apply { roof = value }

    fun build() = House(walls, doors, windows, garage, roof)
}

// -----------------------------
// 4) Adapter
// -----------------------------
interface PaymentGateway {
    fun pay(amount: Double): String
}

class LegacyBankService {
    fun transfer(value: Double): String = "Transferência bancária de R$ $value"
}

class BankAdapter(private val legacyBankService: LegacyBankService) : PaymentGateway {
    override fun pay(amount: Double): String {
        return legacyBankService.transfer(amount)
    }
}

// -----------------------------
// 5) Decorator
// -----------------------------
interface Coffee {
    fun cost(): Double
    fun description(): String
}

class SimpleCoffee : Coffee {
    override fun cost() = 5.0
    override fun description() = "Café simples"
}

abstract class CoffeeDecorator(private val base: Coffee) : Coffee {
    override fun cost() = base.cost()
    override fun description() = base.description()
}

class MilkDecorator(base: Coffee) : CoffeeDecorator(base) {
    override fun cost() = super.cost() + 2.0
    override fun description() = "${super.description()} com leite"
}

class SugarDecorator(base: Coffee) : CoffeeDecorator(base) {
    override fun cost() = super.cost() + 1.0
    override fun description() = "${super.description()} com açúcar"
}

// -----------------------------
// 6) Strategy
// -----------------------------
interface ShippingStrategy {
    fun calculateCost(orderTotal: Double): Double
}

class StandardShipping : ShippingStrategy {
    override fun calculateCost(orderTotal: Double) = 15.0
}

class ExpressShipping : ShippingStrategy {
    override fun calculateCost(orderTotal: Double) = 30.0
}

class PickupShipping : ShippingStrategy {
    override fun calculateCost(orderTotal: Double) = 0.0
}

class Order(private val shippingStrategy: ShippingStrategy) {
    fun totalWithShipping(orderTotal: Double): Double {
        return orderTotal + shippingStrategy.calculateCost(orderTotal)
    }
}

// -----------------------------
// 7) Observer
// -----------------------------
interface Observer {
    fun update(temperature: Double)
}

class WeatherStation {
    private val observers = mutableListOf<Observer>()

    fun addObserver(observer: Observer) {
        observers.add(observer)
    }

    fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    fun setTemperature(temperature: Double) {
        observers.forEach { it.update(temperature) }
    }
}

class PhoneDisplay : Observer {
    override fun update(temperature: Double) {
        println("[PhoneDisplay] Temperatura atual: $temperature°C")
    }
}

// -----------------------------
// 8) Facade
// -----------------------------
class VideoFile(val name: String)

class CodecFactory {
    fun extract(file: VideoFile): String = "Codec do arquivo ${file.name}"
}

class BitrateReader {
    fun read(file: VideoFile, codec: String): String = "Leitura do arquivo ${file.name} usando $codec"
}

class AudioMixer {
    fun mix(input: String): String = "Áudio misturado: $input"
}

class VideoConverterFacade {
    fun convert(videoName: String): String {
        val file = VideoFile(videoName)
        val codec = CodecFactory().extract(file)
        val buffer = BitrateReader().read(file, codec)
        return AudioMixer().mix(buffer)
    }
}

// -----------------------------
// 9) Template Method
// -----------------------------
abstract class ReportGenerator {
    fun generateReport(): String {
        return buildHeader() + buildBody() + buildFooter()
    }

    protected abstract fun buildHeader(): String
    protected abstract fun buildBody(): String
    protected abstract fun buildFooter(): String
}

class SalesReport : ReportGenerator() {
    override fun buildHeader() = "Relatório de Vendas\n"
    override fun buildBody() = "- Produto A: 120\n- Produto B: 80\n"
    override fun buildFooter() = "Total: 200\n"
}

fun showcaseSingleton() {
    val connection = DatabaseConnection.instance
    println(connection.query("SELECT * FROM users"))
}

fun showcaseFactory() {
    val factory = NotificationFactory()
    val email = factory.create("email")
    val sms = factory.create("sms")

    email.send("Olá, mundo!")
    sms.send("Seu código é 1234")
}

fun showcaseBuilder() {
    val house = HouseBuilder()
        .setWalls(4)
        .setDoors(3)
        .setWindows(6)
        .setGarage(true)
        .setRoof("Metálica")
        .build()

    println("Casa construída: $house")
}

fun showcaseAdapter() {
    val legacyService = LegacyBankService()
    val paymentGateway: PaymentGateway = BankAdapter(legacyService)
    println(paymentGateway.pay(150.0))
}

fun showcaseDecorator() {
    val coffee: Coffee = SugarDecorator(
        MilkDecorator(SimpleCoffee())
    )

    println("Descrição: ${coffee.description()}")
    println("Custo: R$ ${coffee.cost()}")
}

fun showcaseStrategy() {
    val orderWithStandardShipping = Order(StandardShipping())
    val orderWithExpressShipping = Order(ExpressShipping())

    println("Standard: ${orderWithStandardShipping.totalWithShipping(100.0)}")
    println("Express: ${orderWithExpressShipping.totalWithShipping(100.0)}")
}

fun showcaseObserver() {
    val station = WeatherStation()
    val display = PhoneDisplay()

    station.addObserver(display)
    station.setTemperature(28.5)
}

fun showcaseFacade() {
    val facade = VideoConverterFacade()
    println(facade.convert("video.mp4"))
}

fun showcaseTemplateMethod() {
    val report = SalesReport()
    println(report.generateReport())
}

fun main() {
    println("=== Design Patterns em Kotlin ===\n")

    println("--- Singleton ---")
    showcaseSingleton()

    println("\n--- Factory Method ---")
    showcaseFactory()

    println("\n--- Builder ---")
    showcaseBuilder()

    println("\n--- Adapter ---")
    showcaseAdapter()

    println("\n--- Decorator ---")
    showcaseDecorator()

    println("\n--- Strategy ---")
    showcaseStrategy()

    println("\n--- Observer ---")
    showcaseObserver()

    println("\n--- Facade ---")
    showcaseFacade()

    println("\n--- Template Method ---")
    showcaseTemplateMethod()
}
