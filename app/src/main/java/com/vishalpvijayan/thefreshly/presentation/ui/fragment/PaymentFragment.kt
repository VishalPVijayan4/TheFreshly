package com.vishalpvijayan.thefreshly.presentation.ui.fragment


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
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
            startPayment()
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

    private fun startPayment() {
        val activity = activity
        if (activity == null || !isAdded || activity.isFinishing || activity.isDestroyed) {
            Toast.makeText(context, "Activity not in valid state", Toast.LENGTH_SHORT).show()
            return
        }

        val checkout = Checkout().apply {
            setKeyID("rzp_test_hOnOpLwgUuTWTI")
        }

        try {
            viewLifecycleOwner.lifecycleScope.launch {
                val totalPrice = cartViewModel.totalCartPrice.value
                val deliveryCharges = checkoutViewModel.deliveryCharges
                val cgst = totalPrice * 0.09
                val sgst = totalPrice * 0.09
                val grandTotal = totalPrice + cgst + sgst + deliveryCharges

                val amountInPaise = (grandTotal * 100).toLong()

                val options = JSONObject().apply {
                    put("name", "Freshly")
                    put("description", "Grocery Payment")
                    put("image", "https://i.imgur.com/A1xyzBq.png")
                    put("theme.color", "#A0522D")
                    put("currency", "INR")
                    put("amount", amountInPaise.toString())

                    // Add notes with delivery details
                    put("notes", JSONObject().apply {
                        put("address", checkoutViewModel.selectedAddress.value?.address ?: "")
                        put("instructions", deliveryInstructions)
                    })

                    put("prefill", JSONObject().apply {
                        put("email", "test@razorpay.com")
                        put("contact", "6361777579")
                    })
                }

                checkout.open(activity, options)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Payment error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        if (!isAdded) return

        viewLifecycleOwner.lifecycleScope.launch {
            // Clear cart after successful payment
            cartViewModel.clearCart()
        }

        Toast.makeText(context, "Payment Success: $razorpayPaymentID", Toast.LENGTH_LONG).show()
        // Navigate to success screen if you have one
        // findNavController().navigate(R.id.action_payment_to_success)
    }

    override fun onPaymentError(code: Int, response: String?) {
        if (!isAdded) return
        Toast.makeText(context, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





