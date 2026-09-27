package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DelayProjectEntity
import com.example.data.PeymanDatabase
import com.example.data.PeymanRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PeymanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PeymanRepository

    init {
        val dao = PeymanDatabase.getDatabase(application).delayProjectDao()
        repository = PeymanRepository(dao)
    }

    val savedProjects: StateFlow<List<DelayProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- State for Directive 5090 Calculator ---
    private val _projectTitle = MutableStateFlow("پروژه احداث ساختمان اداری")
    val projectTitle = _projectTitle.asStateFlow()

    private val _contractorName = MutableStateFlow("شرکت مهندسی عمران‌یار")
    val contractorName = _contractorName.asStateFlow()

    private val _employerName = MutableStateFlow("اداره کل راه و شهرسازی")
    val employerName = _employerName.asStateFlow()

    private val _contractAmountP = MutableStateFlow(50_000_000_000L) // 50 میلیارد ریال
    val contractAmountP = _contractAmountP.asStateFlow()

    private val _initialDurationDays = MutableStateFlow(365) // 1 سال
    val initialDurationDays = _initialDurationDays.asStateFlow()

    private val _advancePayments = MutableStateFlow<List<AdvancePaymentItem>>(
        listOf(
            AdvancePaymentItem(
                installmentName = "قسط اول پیش‌پرداخت (تحویل زمین)",
                requestedAmount = 4_000_000_000L, // 4 میلیارد ریال
                submissionDaysOffset = 10,
                delayDays = 45,
                notes = "تاخیر کارفرما در تامین اعتبار پیش‌پرداخت"
            )
        )
    )
    val advancePayments = _advancePayments.asStateFlow()

    private val _interimInvoices = MutableStateFlow<List<InterimInvoiceItem>>(
        listOf(
            InterimInvoiceItem(
                invoiceNumber = "صورت‌وضعیت موقت شماره ۱",
                grossWorkAmountB = 3_200_000_000L,
                unpaidAmountA = 2_800_000_000L,
                delayDays = 28,
                notes = "تاخیر ۲۰ روزه در رسیدگی و ۸ روزه در پرداخت"
            ),
            InterimInvoiceItem(
                invoiceNumber = "صورت‌وضعیت موقت شماره ۲",
                grossWorkAmountB = 4_500_000_000L,
                unpaidAmountA = 3_900_000_000L,
                delayDays = 35,
                notes = "عدم پرداخت علی‌الحساب ۷۰٪ توسط کارفرما"
            ),
            InterimInvoiceItem(
                invoiceNumber = "صورت‌وضعیت موقت شماره ۳",
                grossWorkAmountB = 5_100_000_000L,
                unpaidAmountA = 4_200_000_000L,
                delayDays = 42,
                notes = "پرداخت با تاخیر ۴۲ روزه از موعد ماده ۳۷"
            )
        )
    )
    val interimInvoices = _interimInvoices.asStateFlow()

    private val _overlapDays = MutableStateFlow(12.0) // روزهای کسر همپوشانی بازه‌های همزمان
    val overlapDays = _overlapDays.asStateFlow()

    // --- State for Fehrest Baha Explorer ---
    val allFehrestItems = repository.getSampleFehrestItems()
    private val _selectedDiscipline = MutableStateFlow(FehrestDiscipline.ABNIYEH)
    val selectedDiscipline = _selectedDiscipline.asStateFlow()

    private val _fehrestSearchQuery = MutableStateFlow("")
    val fehrestSearchQuery = _fehrestSearchQuery.asStateFlow()

    // --- State for Contract Coefficients ---
    private val _coefficients = MutableStateFlow(ContractCoefficients())
    val coefficients = _coefficients.asStateFlow()

    private val _baseEstimateAmount = MutableStateFlow(20_000_000_000L) // 20 میلیارد ریال
    val baseEstimateAmount = _baseEstimateAmount.asStateFlow()

    val regionalFactors = repository.getRegionalFactors()
    private val _selectedRegionName = MutableStateFlow("تهران و مراکز استان‌های مرکزی")
    val selectedRegionName = _selectedRegionName.asStateFlow()

    // --- State for Price Adjustment (تعدیل) ---
    private val _baseIndexI0 = MutableStateFlow(1500.0) // شاخص مبنای پیمان
    val baseIndexI0 = _baseIndexI0.asStateFlow()

    private val _periodIndexI = MutableStateFlow(2250.0) // شاخص دوره کارکرد
    val periodIndexI = _periodIndexI.asStateFlow()

    private val _invoiceGrossWork = MutableStateFlow(5_000_000_000L) // مبلغ ناخالص صورت وضعیت
    val invoiceGrossWork = _invoiceGrossWork.asStateFlow()

    private val _disciplineFactor = MutableStateFlow(0.95) // ضریب شاخص رشته مربوطه
    val disciplineFactor = _disciplineFactor.asStateFlow()

    // --- Bank of Regulations ---
    val legalArticles = repository.getLegalArticles()
    val claimChecklist = repository.getClaimChecklist()

    // --- Feedback message ---
    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage = _statusMessage.asStateFlow()

    // --- Updaters for 5090 Calculator ---
    fun updateProjectTitle(title: String) { _projectTitle.value = title }
    fun updateContractorName(name: String) { _contractorName.value = name }
    fun updateEmployerName(name: String) { _employerName.value = name }

    fun updateContractAmountP(amount: Long) {
        if (amount >= 0) _contractAmountP.value = amount
    }

    fun updateInitialDurationDays(days: Int) {
        if (days > 0) _initialDurationDays.value = days
    }

    fun updateOverlapDays(days: Double) {
        if (days >= 0) _overlapDays.value = days
    }

    fun addAdvancePayment(item: AdvancePaymentItem) {
        _advancePayments.value = _advancePayments.value + item
    }

    fun removeAdvancePayment(id: String) {
        _advancePayments.value = _advancePayments.value.filterNot { it.id == id }
    }

    fun updateAdvancePayment(item: AdvancePaymentItem) {
        _advancePayments.value = _advancePayments.value.map {
            if (it.id == item.id) item else it
        }
    }

    fun addInterimInvoice(item: InterimInvoiceItem) {
        _interimInvoices.value = _interimInvoices.value + item
    }

    fun removeInterimInvoice(id: String) {
        _interimInvoices.value = _interimInvoices.value.filterNot { it.id == id }
    }

    fun updateInterimInvoice(item: InterimInvoiceItem) {
        _interimInvoices.value = _interimInvoices.value.map {
            if (it.id == item.id) item else it
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }

    /**
     * خلاصه محاسبات تمدید پیمان بر اساس بخشنامه ۵۰۹۰
     */
    fun calculateSummary(): DelayClaimSummary {
        val p = _contractAmountP.value
        val totalAdvanceDays = _advancePayments.value.sumOf { it.calculateExtensionDays(p) }
        val totalInvoiceDays = _interimInvoices.value.sumOf { it.calculateExtensionDays() }
        val rawTotal = totalAdvanceDays + totalInvoiceDays
        val overlap = _overlapDays.value.coerceAtMost(rawTotal)
        val netAllowed = (rawTotal - overlap).coerceAtLeast(0.0)

        val initialDays = _initialDurationDays.value
        val newDuration = initialDays + netAllowed
        val extensionPercent = if (initialDays > 0) (netAllowed / initialDays) * 100.0 else 0.0

        return DelayClaimSummary(
            totalAdvanceExtensionDays = totalAdvanceDays,
            totalInvoiceExtensionDays = totalInvoiceDays,
            totalRawExtensionDays = rawTotal,
            overlapDeductionDays = overlap,
            netAllowedExtensionDays = netAllowed,
            extensionPercentageOfContract = extensionPercent,
            initialDurationDays = initialDays,
            newTotalDurationDays = newDuration
        )
    }

    fun saveCurrentProjectToDb() {
        viewModelScope.launch {
            val summary = calculateSummary()
            val entity = DelayProjectEntity(
                title = _projectTitle.value,
                contractor = _contractorName.value,
                employer = _employerName.value,
                contractAmountP = _contractAmountP.value,
                initialDurationDays = _initialDurationDays.value,
                advancePaymentsData = "AdvanceCount:${_advancePayments.value.size}",
                interimInvoicesData = "InvoiceCount:${_interimInvoices.value.size}",
                calculatedExtensionDays = summary.netAllowedExtensionDays
            )
            repository.saveProject(entity)
            _statusMessage.value = "پرونده پیمان با موفقیت ذخیره گردید."
        }
    }

    fun deleteSavedProject(id: Long) {
        viewModelScope.launch {
            repository.deleteProject(id)
            _statusMessage.value = "پرونده حذف گردید."
        }
    }

    // --- Fehrest Baha Handlers ---
    fun setFehrestDiscipline(discipline: FehrestDiscipline) {
        _selectedDiscipline.value = discipline
    }

    fun setFehrestSearchQuery(query: String) {
        _fehrestSearchQuery.value = query
    }

    fun getFilteredFehrestItems(): List<FehrestItem> {
        val disc = _selectedDiscipline.value
        val query = _fehrestSearchQuery.value.trim()
        val baseList = allFehrestItems.filter { it.discipline == disc }
        return if (query.isEmpty()) {
            baseList
        } else {
            baseList.filter {
                it.code.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true) ||
                it.chapter.contains(query, ignoreCase = true)
            }
        }
    }

    // --- Coefficients Handlers ---
    fun updateBaseEstimate(amount: Long) {
        if (amount >= 0) _baseEstimateAmount.value = amount
    }

    fun updateIsGovernmental(isGov: Boolean) {
        _coefficients.value = _coefficients.value.copy(
            isGovernmentalProject = isGov,
            customOverhead = if (isGov) 1.41 else 1.30
        )
    }

    fun updateSelectedRegion(regionName: String) {
        _selectedRegionName.value = regionName
        val factor = regionalFactors[regionName] ?: 1.00
        _coefficients.value = _coefficients.value.copy(regionalFactor = factor)
    }

    fun updateTenderFactor(factor: Double) {
        _coefficients.value = _coefficients.value.copy(contractorTenderFactor = factor)
    }

    fun updateFloorFactor(factor: Double) {
        _coefficients.value = _coefficients.value.copy(floorFactor = factor)
    }

    fun updateHeightFactor(factor: Double) {
        _coefficients.value = _coefficients.value.copy(heightFactor = factor)
    }

    fun updateMobilizationPercent(percent: Double) {
        _coefficients.value = _coefficients.value.copy(siteMobilizationPercentage = percent)
    }

    // --- Price Adjustment (تعدیل) Handlers ---
    fun updateBaseIndexI0(v: Double) { if (v > 0) _baseIndexI0.value = v }
    fun updatePeriodIndexI(v: Double) { if (v > 0) _periodIndexI.value = v }
    fun updateInvoiceGrossWork(amount: Long) { if (amount >= 0) _invoiceGrossWork.value = amount }
    fun updateDisciplineFactor(v: Double) { if (v > 0) _disciplineFactor.value = v }

    /**
     * محاسبه مبلغ تعدیل
     * E = B * ( (I / I0) - 1 ) * ضریب رشته
     */
    fun calculateAdjustmentAmount(): Long {
        val i0 = _baseIndexI0.value
        val i = _periodIndexI.value
        val b = _invoiceGrossWork.value
        val discFactor = _disciplineFactor.value
        if (i0 <= 0 || b <= 0) return 0L
        val ratio = (i / i0) - 1.0
        val adj = b.toDouble() * ratio * discFactor
        return adj.toLong()
    }
}
