package com.example

import com.example.model.AdvancePaymentItem
import com.example.model.ContractCoefficients
import com.example.model.InterimInvoiceItem
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testDirective5090AdvancePaymentFormula() {
        // T = (A * (t2 - t1)) / P
        // A = 5,000,000,000 , t2 - t1 = 30 days , P = 50,000,000,000
        // Expected T = (5 * 30) / 50 = 3.0 days
        val item = AdvancePaymentItem(
            requestedAmount = 5_000_000_000L,
            delayDays = 30
        )
        val extDays = item.calculateExtensionDays(50_000_000_000L)
        assertEquals(3.0, extDays, 0.001)
    }

    @Test
    fun testDirective5090InterimInvoiceFormula() {
        // T = (A * (t2 - t1)) / B
        // B = 4,000,000,000 , A = 2,000,000,000 , delay = 20 days
        // Expected T = (2 * 20) / 4 = 10.0 days
        val item = InterimInvoiceItem(
            grossWorkAmountB = 4_000_000_000L,
            unpaidAmountA = 2_000_000_000L,
            delayDays = 20
        )
        val extDays = item.calculateExtensionDays()
        assertEquals(10.0, extDays, 0.001)
    }

    @Test
    fun testContractCoefficients() {
        val coef = ContractCoefficients(
            isGovernmentalProject = true, // 1.41
            regionalFactor = 1.10,
            contractorTenderFactor = 0.90,
            floorFactor = 1.00,
            heightFactor = 1.00,
            siteMobilizationPercentage = 4.0
        )
        // Multiplier = 1.41 * 1.10 * 0.90 = 1.3959
        assertEquals(1.3959, coef.totalContractMultiplier(), 0.001)
    }
}
