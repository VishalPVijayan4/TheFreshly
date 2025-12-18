package com.vishalpvijayan.thefreshly.presentation.ui.fragment


import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.vishalpvijayan.thefreshly.R
import com.vishalpvijayan.thefreshly.databinding.FragmentPaymentBinding
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class PaymentFragment : Fragment(), PaymentResultListener {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preload Razorpay checkout resources
        Checkout.preload(requireContext())

        binding.btnPay.setOnClickListener {
            startPayment()
        }
    }

    private fun startPayment() {
        val activity = activity
        if (activity == null || !isAdded || activity.isFinishing || activity.isDestroyed) {
            Toast.makeText(context, "Activity not in valid state", Toast.LENGTH_SHORT).show()
            return
        }

        val checkout = Checkout().apply {
//            setKeyID("rzp_test_hOnOpLwgUuTWTI,UlWLL9jdKSt1CYv3KYRVHoYi") // Replace with your key
            setKeyID("rzp_test_hOnOpLwgUuTWTI" +
                    "") // Replace with your key
        }

        try {
            val options = JSONObject().apply {
                put("name", "Freshly")
                put("description", "Test Payment")
                put("image", "https://i.imgur.com/A1xyzBq.png")
                put("theme.color", "#A0522D")
                put("currency", "INR")
                put("amount", "50000") // ₹500.00 in paise

                put("prefill", JSONObject().apply {
                    put("email", "test@razorpay.com")
                    put("contact", "6361777579")
                })
            }

            checkout.open(activity, options)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Payment error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onPaymentSuccess(razorpayPaymentID: String) {
        if (!isAdded) return
        Toast.makeText(context, "Payment Success: $razorpayPaymentID", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(code: Int, response: String?) {
        if (!isAdded) return
        Toast.makeText(context, "Payment Failed: $response", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getBase64Logo(): String {
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.image_icon)!!
        val bitmap = (drawable as BitmapDrawable).bitmap
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}




