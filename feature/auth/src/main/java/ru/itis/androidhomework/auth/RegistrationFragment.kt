package ru.itis.androidhomework.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.itis.androidhomework.auth.databinding.FragmentRegistrationBinding
import ru.itis.androidhomework.base.BaseFragment


@AndroidEntryPoint
class RegistrationFragment : BaseFragment(R.layout.fragment_registration) {

    private var viewBinding: FragmentRegistrationBinding? = null

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.apply {
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val userName = etName.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && userName.isNotEmpty()) {
                    viewModel.registerUser(email = email, password = password, userName = userName)
                } else {
                    showToast(getString(R.string.zapolni))
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.registrationState.collect { state ->
                when (state) {
                    is RegistrationState.Success -> {
                        viewModel.goToLoginPage()
                    }
                    is RegistrationState.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.fail), Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}