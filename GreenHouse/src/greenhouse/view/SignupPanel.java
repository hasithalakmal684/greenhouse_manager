/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.view;

import greenhouse.dao.UsersDAO;
import greenhouse.model.UserDetails;
import greenhouse.model.Users;
import greenhouse.utility.HTMLEmail;
import greenhouse.utility.Validator;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Hasitha Lakmal
 */
public class SignupPanel extends javax.swing.JPanel {

    JFrame parent;
    BufferedImage correctImg;
    BufferedImage wrongImg;
    ImageIcon correctIcon;
    ImageIcon wrongIcon;

    /**
     * Creates new form SignupPanel
     */
    public SignupPanel() {
        initComponents();
        nameTextField.requestFocus();

        nameValidityLabel.setSize(24, 24);
        emailValidityLabel.setSize(24, 24);
        telValidityLabel.setSize(24, 24);
        try {
            correctImg = ImageIO.read(new File("./images/correct.gif"));
            wrongImg = ImageIO.read(new File("./images/wrong.png"));
            correctIcon = new ImageIcon(correctImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
            wrongIcon = new ImageIcon(wrongImg.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SignupPanel(JFrame parent) {
        this();
        this.parent = parent;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        emailTextField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        confirmPasswordField = new javax.swing.JPasswordField();
        submitButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        nameValidityLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        addressTextArea = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        telTextField = new javax.swing.JTextField();
        emailValidityLabel = new javax.swing.JLabel();
        telValidityLabel = new javax.swing.JLabel();
        passwordValidityLabel = new javax.swing.JLabel();

        jLabel1.setText("Name :");

        jLabel2.setText("Email * :");

        jLabel3.setText("Password * :");

        jLabel4.setText("Confirm Password * :");

        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        nameTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nameTextFieldKeyReleased(evt);
            }
        });

        emailTextField.setText("Ex : abc@example.com");
        emailTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextFieldActionPerformed(evt);
            }
        });
        emailTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emailTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailTextFieldKeyReleased(evt);
            }
        });

        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        confirmPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPasswordFieldActionPerformed(evt);
            }
        });
        confirmPasswordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                confirmPasswordFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmPasswordFieldKeyReleased(evt);
            }
        });

        submitButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("Address :");

        addressTextArea.setColumns(20);
        addressTextArea.setRows(5);
        jScrollPane1.setViewportView(addressTextArea);

        jLabel6.setText("Telephone :");

        telTextField.setText("0#########");
        telTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telTextFieldActionPerformed(evt);
            }
        });
        telTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                telTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                telTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameTextField)
                            .addComponent(emailTextField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(telTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(telValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordField)
                                    .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameValidityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(emailValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(submitButton)
                        .addGap(2, 2, 2)
                        .addComponent(cancelButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(telTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(telValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(confirmPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(passwordValidityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        boolean isNameValidated = Validator.validateName(nameTextField.getText());
        boolean isEmailValidated = Validator.validateEmail(emailTextField.getText());
        boolean isTelValidated = Validator.validateTelephone(telTextField.getText());

        String password = new String(passwordField.getPassword());
        String confPassword = new String(confirmPasswordField.getPassword());

        boolean isPasswordValid = password.equals(confPassword);

        if (isEmailValidated && isPasswordValid) {
            if (nameTextField.getText().length() > 0 || telTextField.getText().length() > 0) {
                if (nameTextField.getText().length() > 0 && !isNameValidated) {
                    JOptionPane.showMessageDialog(parent, "Please confirm the validity of name.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
                if (telTextField.getText().length() > 0 && !isTelValidated) {
                    JOptionPane.showMessageDialog(parent, "Please confirm the validity of telephone number.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }

            try {
                String name = nameTextField.getText();
                String email = emailTextField.getText();
                String address = addressTextArea.getText();
                String tel = telTextField.getText();
                String altPassword = RandomStringUtils.randomAlphanumeric(10);

                Users user = new Users();
                user.setEmail(email);
                user.setPassword(DigestUtils.sha1Hex(password));
                user.setUsed(0);
                user.setId(RandomStringUtils.randomAlphanumeric(10));
                user.setAdmin(0);
                user.setAltpassword(DigestUtils.sha1Hex(altPassword));

                UserDetails details = new UserDetails();
                details.setName(name);
                details.setTelephone(tel);
                details.setAddress(address);

                user.setUserDetails(details);

                int userExist = UsersDAO.insertUser(user);
                //userExist = 0 -> userExist
                //userExist > 0 -> !userExist
                if (userExist == 0) {
                    int option = JOptionPane.showOptionDialog(parent, "Email Address you entered exists. You can login or send verification email if you forgot password. If you send email, login with emailed credentials. Then use reset password facility to set your password.", "Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"Send Verification Email again.", "Cancel"}, null);
                    if (option == 0) {
                        int rep = UsersDAO.updateAltPassword(email, altPassword);
                        HTMLEmail hTMLEmail = new HTMLEmail();
                        hTMLEmail.sendAccountVerificationEmail(email, altPassword);
                    }
                } else {
                    HTMLEmail hTMLEmail = new HTMLEmail();
                    hTMLEmail.sendAccountVerificationEmail(email, altPassword);
                    JOptionPane.showMessageDialog(parent, "Verification email sent to your email. Use emailed credentials to log in to system.");
                }
                
                nameTextField.setText(null);
                emailTextField.setText(null);
                addressTextArea.setText(null);
                telTextField.setText(null);
                passwordField.setText(null);
                confirmPasswordField.setText(null);
                nameValidityLabel.setIcon(null);
                emailValidityLabel.setIcon(null);
                telValidityLabel.setIcon(null);
                passwordValidityLabel.setIcon(null);
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SignupPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SignupPanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SignupPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            JOptionPane.showMessageDialog(parent, "Please confirm the validity of email and password.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

    }//GEN-LAST:event_submitButtonActionPerformed

    private void nameTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyPressed
        if (Validator.validateName(nameTextField.getText())) {
            nameValidityLabel.setIcon(correctIcon);
        } else {
            nameValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_nameTextFieldKeyPressed

    private void nameTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextFieldKeyReleased
        if (Validator.validateName(nameTextField.getText())) {
            nameValidityLabel.setIcon(correctIcon);
        } else {
            nameValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_nameTextFieldKeyReleased

    private void emailTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyPressed
        if (Validator.validateEmail(emailTextField.getText())) {
            emailValidityLabel.setIcon(correctIcon);
        } else {
            emailValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_emailTextFieldKeyPressed

    private void emailTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextFieldKeyReleased
        if (Validator.validateEmail(emailTextField.getText())) {
            emailValidityLabel.setIcon(correctIcon);
        } else {
            emailValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_emailTextFieldKeyReleased

    private void telTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telTextFieldKeyPressed
        if (Validator.validateTelephone(telTextField.getText())) {
            telValidityLabel.setIcon(correctIcon);
        } else {
            telValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_telTextFieldKeyPressed

    private void telTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_telTextFieldKeyReleased
        if (Validator.validateTelephone(telTextField.getText())) {
            telValidityLabel.setIcon(correctIcon);
        } else {
            telValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_telTextFieldKeyReleased

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        if (Validator.validateName(nameTextField.getText())) {
            emailTextField.requestFocus();
            emailValidityLabel.setIcon(correctIcon);
        } else {
            nameTextField.requestFocus();
            nameValidityLabel.setIcon(wrongIcon);
            nameTextField.selectAll();
            nameValidityLabel.setToolTipText("Must be a valid name.");
        }
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void emailTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextFieldActionPerformed
        if (Validator.validateEmail(emailTextField.getText())) {
            addressTextArea.requestFocus();
            emailValidityLabel.setIcon(correctIcon);
        } else {
            emailTextField.requestFocus();
            emailValidityLabel.setIcon(wrongIcon);
            emailTextField.selectAll();
            emailValidityLabel.setToolTipText("Must be a valid name.");
        }
    }//GEN-LAST:event_emailTextFieldActionPerformed

    private void telTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telTextFieldActionPerformed
        if (Validator.validateTelephone(telTextField.getText())) {
            passwordField.requestFocus();
            telValidityLabel.setIcon(correctIcon);
        } else {
            telTextField.requestFocus();
            telValidityLabel.setIcon(wrongIcon);
            telTextField.selectAll();
            telValidityLabel.setToolTipText("Must be a valid name.");
        }
    }//GEN-LAST:event_telTextFieldActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        confirmPasswordField.requestFocus();
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void confirmPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPasswordFieldActionPerformed
        String password = new String(passwordField.getPassword());
        String confPassword = new String(confirmPasswordField.getPassword());
        if (password.equals(confPassword)) {
            passwordValidityLabel.setIcon(correctIcon);
        } else {
            passwordValidityLabel.setIcon(wrongIcon);
            confirmPasswordField.requestFocus();
            confirmPasswordField.selectAll();
        }
    }//GEN-LAST:event_confirmPasswordFieldActionPerformed

    private void confirmPasswordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordFieldKeyPressed
        String password = new String(passwordField.getPassword());
        String confPassword = new String(confirmPasswordField.getPassword());
        if (password.equals(confPassword)) {
            passwordValidityLabel.setIcon(correctIcon);
        } else {
            passwordValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_confirmPasswordFieldKeyPressed

    private void confirmPasswordFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswordFieldKeyReleased
        String password = new String(passwordField.getPassword());
        String confPassword = new String(confirmPasswordField.getPassword());
        if (password.equals(confPassword)) {
            passwordValidityLabel.setIcon(correctIcon);
//            submitButton.doClick();
        } else {
            passwordValidityLabel.setIcon(wrongIcon);
        }
    }//GEN-LAST:event_confirmPasswordFieldKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addressTextArea;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel emailValidityLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel nameValidityLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordValidityLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextField telTextField;
    private javax.swing.JLabel telValidityLabel;
    // End of variables declaration//GEN-END:variables
}
