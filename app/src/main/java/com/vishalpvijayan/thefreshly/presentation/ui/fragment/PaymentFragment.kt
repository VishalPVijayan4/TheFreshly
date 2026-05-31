package com.vishalpvijayan.thefreshly.presentation.ui.fragment


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.vishalpvijayan.thefreshly.MainActivity
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentPaymentBinding
import com.vishalpvijayan.thefreshly.presentation.vm.CartViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.CheckoutViewModel
import com.vishalpvijayan.thefreshly.presentation.vm.ToolbarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream


@AndroidEntryPoint
class PaymentFragment : Fragment(), PaymentResultListener {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private val toolbarViewModel: ToolbarViewModel by activityViewModels()
    private val checkoutViewModel: CheckoutViewModel by activityViewModels()

    private var deliveryInstructions: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        toolbarViewModel.setToolbarTitle("CheckOut ", "Cross check details before proceeding")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Checkout.preload(requireContext())

        observeCartData()
        observeAddressData()
        setupDeliveryInstructions()

        binding.btnPay.setOnClickListener {
//            startPayment()
            MockPayment()
        }
    }

    private fun observeCartData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    cartViewModel.totalCartPrice.collect { total ->
                        updatePriceBreakdown(total)
                    }
                }
            }
        }
    }

    private fun updatePriceBreakdown(productTotal: Double) {
        val deliveryCharges = checkoutViewModel.deliveryCharges
        val cgst = productTotal * 0.09 // 9% CGST
        val sgst = productTotal * 0.09 // 9% SGST
        val grandTotal = productTotal + cgst + sgst + deliveryCharges

        binding.txt3.text = "₹${"%.2f".format(productTotal)}"
        binding.txt5.text = "₹${"%.2f".format(cgst)}"
        binding.txt7.text = "₹${"%.2f".format(sgst)}"
        binding.txt9.text = "₹${"%.2f".format(deliveryCharges)}"
        binding.txttotalPrice.text = "₹${"%.2f".format(grandTotal)}"
    }

    private fun observeAddressData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                checkoutViewModel.selectedAddress.collect { address ->
                    if (address != null) {
                        binding.txtAddressDefault.text = address.address
                        binding.txtActualAddress.text = address.address
                    } else {
                        binding.txtAddressDefault.text = getString(R.string.default_address)
                    }
                }
            }
        }

        // Change address button
        binding.btnAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_payment_to_mapFragment)
        }
    }

    private fun setupDeliveryInstructions() {
        binding.btnSubmitInstruction.setOnClickListener {
            val instruction = binding.inputEmail.text.toString().trim()
            if (instruction.isNotEmpty()) {
                deliveryInstructions = instruction
                checkoutViewModel.setDeliveryInstructions(instruction)
                Toast.makeText(context, "Delivery instructions saved", Toast.LENGTH_SHORT).show()

                // Hide keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.inputEmail.windowToken, 0)
            }
        }
    }

    private fun MockPayment() {
        // Show bottom sheet dialog with payment options
        showPaymentSelectionDialog()
    }

    private fun showPaymentSelectionDialog() {
        val bottomSheetDialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val dialogBinding = com.vishalpvijayan.thefreshly.databinding.BottomSheetPaymentSelectionBinding.inflate(layoutInflater)

        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.btnPaymentSuccess.setOnClickListener {
            bottomSheetDialog.dismiss()
            handlePaymentSuccess()
        }

        dialogBinding.btnPaymentFail.setOnClickListener {
            bottomSheetDialog.dismiss()
            showPaymentFailedDialog()
        }

        bottomSheetDialog.show()
    }

    private fun handlePaymentSuccess() {
        viewLifecycleOwner.lifecycleScope.launch {
            Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show()
            // Navigate to order success screen
            findNavController().navigate(R.id.action_payment_to_orderSuccessFragment)
        }
    }

    private fun showPaymentFailedDialog() {
        val dialog = android.app.Dialog(requireContext())
        val dialogBinding = com.vishalpvijayan.thefreshly.databinding.DialogPaymentFailedBinding.inflate(layoutInflater)

        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        dialogBinding.btnRetry.setOnClickListener {
            dialog.dismiss()
            // Show payment selection dialog again
            showPaymentSelectionDialog()
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(context, "Payment cancelled", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }


    private fun startPayment() {
        Log.d("PaymentFragment", "🔥 startPayment() called")

        val activity = requireActivity() // Use requireActivity() for non-null
        if (!isAdded || activity.isFinishing || activity.isDestroyed) {
            Log.e("PaymentFragment", "❌ Activity/Fragment invalid - isAdded: $isAdded, isFinishing: ${activity.isFinishing}, isDestroyed: ${activity.isDestroyed}")
            Toast.makeText(context, "Activity not in valid state", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("PaymentFragment", "✅ Activity valid, preloading Checkout...")

        // Preload FIRST - Critical for white screen fix
        Checkout.preload(activity)
        Log.d("PaymentFragment", "✅ Checkout preloaded")

        val checkout = Checkout().apply {
            setKeyID("rzp_test_RvYknb590BqzaM")
        }
        Log.d("PaymentFragment", "✅ Checkout instance created")

        try {
            viewLifecycleOwner.lifecycleScope.launch {
                Log.d("PaymentFragment", "🚀 Creating payment options...")

                val totalPrice = cartViewModel.totalCartPrice.value
                val deliveryCharges = checkoutViewModel.deliveryCharges
                val cgst = totalPrice * 0.09
                val sgst = totalPrice * 0.09
                val grandTotal = totalPrice + cgst + sgst + deliveryCharges
                val amountInPaise = (grandTotal * 100).toLong()

                Log.d("PaymentFragment", "💰 Totals - totalPrice: $totalPrice, grandTotal: $grandTotal, amountInPaise: $amountInPaise")

                val options = JSONObject().apply {
                    put("name", "Freshly")
                    put("image", "https://picsum.photos/200/300")
                    put("description", "Grocery Payment")
                    put("order_id", 44550)
                    put("theme.color", "#A0522D")
                    put("currency", "INR")
                    put("amount", amountInPaise.toString())

                    put("notes", JSONObject().apply {
                        put("address", checkoutViewModel.selectedAddress.value?.address ?: "")
                        put("instructions", deliveryInstructions)
                    })

                    put("prefill", JSONObject().apply {
                        put("email", "test@razorpay.com")
                        put("contact", "6361777579")
                    })
                }

                Log.d("PaymentFragment", "📦 Options created: ${options.toString()}")
                Log.d("PaymentFragment", "🎯 Opening Checkout...")

//                checkout.open(activity, options)
//                checkout.open(requireActivity(), options)
                (requireActivity() as MainActivity).startRazorpayPayment(options)

                Log.d("PaymentFragment", "✅ Checkout.open() called successfully")
            }
        } catch (e: Exception) {
            Log.e("PaymentFragment", "💥 Payment setup ERROR", e)
            Toast.makeText(context, "Payment error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        Log.d("PaymentFragment", "✅✅ PAYMENT SUCCESS: $razorpayPaymentID")

        if (!isAdded) {
            Log.w("PaymentFragment", "⚠️ Fragment not added, skipping success handling")
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            Log.d("PaymentFragment", "🧹 Clearing cart...")
            cartViewModel.clearCart()
            Log.d("PaymentFragment", "✅ Cart cleared")
        }

        Toast.makeText(context, "Payment Success: $razorpayPaymentID", Toast.LENGTH_LONG).show()
        Log.d("PaymentFragment", "🎉 Success toast shown")
        // findNavController().navigate(R.id.action_payment_to_success)
    }

    override fun onPaymentError(code: Int, response: String?) {
        Log.e("PaymentFragment", "❌❌ PAYMENT ERROR - Code: $code, Response: $response")

        if (!isAdded) {
            Log.w("PaymentFragment", "⚠️ Fragment not added, skipping error handling")
            return
        }

        val errorMsg = when (code) {
            0 -> "Payment cancelled by user"
            1 -> "Payment failed - network error"
            else -> "Payment failed: $response (Code: $code)"
        }

        Log.e("PaymentFragment", "💥 Final error message: $errorMsg")
        Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





